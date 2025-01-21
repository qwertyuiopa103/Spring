package ispan.user.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ispan.user.service.CustomUserDetailsService;
import ispan.user.tools.JwtTool;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * 過濾每個請求，檢查 Authorization 標頭中的 JWT Token
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = null;
        String userId = null;
        String userRole = null;
        // 檢查 Authorization 標頭是否存在且以 "Bearer " 開頭
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                if (JwtTool.isTokenValid(token)) {
                    userId = JwtTool.getSubject(token);
                    userRole = JwtTool.getClaim(token, "userRole"); 
                }
            } catch (Exception e) {
                // Token 無效或過期
                logger.error("JWT Token 無效: {}", e);
                request.setAttribute("exception", e);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 如果 Token 有效且尚未在 SecurityContext 中設置認證
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            if (userDetails != null) {
                // 转换 authorities 为 List<GrantedAuthority>
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                userDetails.getAuthorities().forEach(auth -> 
                    authorities.add(new SimpleGrantedAuthority(auth.getAuthority()))
                );

                // 创建认证令牌
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities
                );

                // 设置到 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}