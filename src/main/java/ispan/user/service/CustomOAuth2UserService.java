package ispan.user.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;


@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 呼叫 Spring 預設的 userService
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 您可以在這裡執行自訂邏輯 (查詢資料庫、更新用戶資訊等)

        return new DefaultOAuth2User(
            oAuth2User.getAuthorities(),
            oAuth2User.getAttributes(),
            "sub" // Google user 的唯一 ID
        );
    }
}

