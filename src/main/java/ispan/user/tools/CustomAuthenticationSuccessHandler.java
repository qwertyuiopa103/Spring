package ispan.user.tools;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;
import ispan.user.service.UserService;
import ispan.user.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserServiceImpl userServiceImpl; // 您的用戶服務

    @Autowired
    private JwtTool jwtTool; // 您的 JWT 工具類
    
    @Autowired
    private UserRepository userRepository;

    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<UserBean> userOpt = userServiceImpl.findByMail(email);

        if (userOpt.isPresent()) {
            // 用戶已存在
        	UserBean user = userOpt.get();
        	

            // 檢查是否被標記為刪除
            if (user.getUserSecurity().isUserDeleted()) {
                response.sendRedirect("http://localhost:5173/#/home/error");
                return; // 停止後續邏輯
            }
        	
        	user.getUserSecurity().setUserLogin(new Timestamp(System.currentTimeMillis()));
        	userRepository.save(user);
            // 如果尚未驗證
//            if (user.isUserVerified() != false) {
//                user.setUserVerified(true);
//                userServiceImpl.insertAndUpate(user);
//            }

            // 產生 JWT
            String token = jwtTool.generateToken(user.getUserID(),user.getUserRole());

            // 將 token 放在 Query Param
            String redirectUrl = "http://localhost:5173/#/home?token="
                + URLEncoder.encode(token, StandardCharsets.UTF_8);

            response.sendRedirect(redirectUrl);

        } else {
            // 用戶不存在，將姓名與信箱放在 Query Param，導到註冊頁
            String redirectUrl = "http://localhost:5173/#/home/userRegister?name="
                + URLEncoder.encode(name, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

            response.sendRedirect(redirectUrl);
        }
    }
}