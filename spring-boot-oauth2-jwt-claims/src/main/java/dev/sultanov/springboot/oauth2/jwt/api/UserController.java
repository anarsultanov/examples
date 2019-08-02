package dev.sultanov.springboot.oauth2.jwt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private TokenStore tokenStore;

    @Autowired
    public UserController(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        Map<String, Object> additionalInfo = getAdditionalInfo(authentication);
        int userId = (int) additionalInfo.get("user_id");
        return ResponseEntity.ok(new UserDto(principal, userId));
    }

    private Map<String, Object> getAdditionalInfo(Authentication authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
        return accessToken.getAdditionalInformation();
    }

    private class UserDto {
        private String username;
        private int userId;

        UserDto(String username, int userId) {
            this.username = username;
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public int getUserId() {
            return userId;
        }
    }
}
