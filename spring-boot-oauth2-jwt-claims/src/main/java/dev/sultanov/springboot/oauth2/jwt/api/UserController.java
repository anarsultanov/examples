package dev.sultanov.springboot.oauth2.jwt.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        return ResponseEntity.ok(new UserDto(principal));
    }

    private class UserDto {
        private String username;

        UserDto(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }
}
