package ispan.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.servlet.HandlerExceptionResolver;

import ispan.user.filter.JwtFilter;
import ispan.user.service.CustomOAuth2UserService;
import ispan.user.tools.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

		 private final JwtFilter jwtFilter;
		 private final HandlerExceptionResolver handlerExceptionResolver;
		 private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
		 private final CustomOAuth2UserService customOAuth2UserService;
	    @Autowired
	    public SecurityConfig(JwtFilter jwtFilter,
	                          HandlerExceptionResolver handlerExceptionResolver,
	                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
	                          CustomOAuth2UserService customOAuth2UserService) {
	        this.jwtFilter = jwtFilter;
	        this.handlerExceptionResolver = handlerExceptionResolver;
	        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
	        this.customOAuth2UserService = customOAuth2UserService;
	    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置 CORS
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173")); // 替換為可信的域名
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 配置開放路徑
       // List<String> allowedURL = List.of("/test/**", "/api/UserNoAuth/**", "/public/**", "/static/**","/api/LoginController/**");

        List<String> allowedURL = List.of("/api/UserNoAuth/**","/api/LoginController/**","/oauth2/**","/api/OrderNoAuth/**","/api/userChat/**","/api/payment/**");

        return http
                .cors(cors -> cors.configurationSource(request -> corsConfiguration)) // 配置 CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 無狀態
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF
                .authorizeHttpRequests(auth -> {
                    // 設置無需認證的路徑
                    allowedURL.forEach(url -> auth.requestMatchers(url).permitAll());

                    // 管理員權限
                    auth.requestMatchers("/api/UserAdmin/**","/api/ReserveAdmin/**","/api/ordersAdmin/**","/api/eventAdmin/**","/api/CaregiverAdmin/**").hasAuthority("ROLE_ADMIN");

                    // 其他請求需要認證
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(customAuthenticationSuccessHandler)
                          .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService));
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // 添加 JWT 過濾器
                .exceptionHandling(exceptionHandling -> {
                    // 未授權訪問處理
                    exceptionHandling.authenticationEntryPoint((req, resp, exception) -> {
                        handlerExceptionResolver.resolveException(req, resp, null, exception);
                    });

                    // 權限不足處理
                    exceptionHandling.accessDeniedHandler((req, resp, exception) -> {
                        handlerExceptionResolver.resolveException(req, resp, null, exception);
                    });
                })
                .build();
    }
    // 提供一個自定義的 OAuth2UserService Bean
   
}