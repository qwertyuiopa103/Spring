package ispan.user.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import ispan.user.model.UserBean;
import ispan.user.model.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    private static final String ADMIN_USER_ID = "USR0001"; // 指定的管理員用戶 ID

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserBean> userOpt = userRepository.findById(userId);

        if (!userOpt.isPresent()) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }

        UserBean user = userOpt.get();

        // 判斷用戶是否被鎖定
        Timestamp lockoutEnd = user.getUserLockoutEnd();
        boolean isLocked = false;
        if (lockoutEnd != null) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            if (lockoutEnd.after(currentTime)) {
                isLocked = true;
            } 
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole()));

        // 如果是管理員，添加管理員權限
//        if (ADMIN_USER_ID.equals(userId)) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        }

        return new User(
        		user.getUserID(),
                user.getUserPassword(),
                user.isUserActive(),    // 帳號是否啟用
                true,                   // 帳號是否未過期
                true,                   // 密碼是否未過期
                !isLocked,             // 帳號是否未鎖定
                authorities             // 使用者角色和額外的管理員角色
        );
    }
}
