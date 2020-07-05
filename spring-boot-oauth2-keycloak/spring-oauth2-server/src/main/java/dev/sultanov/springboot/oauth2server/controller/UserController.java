package dev.sultanov.springboot.oauth2server.controller;

import dev.sultanov.springboot.oauth2server.controller.dto.PasswordDto;
import dev.sultanov.springboot.oauth2server.controller.dto.UserDetailsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable("username") String username) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            List<String> authorities = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new UserDetailsDto(userDetails.getUsername(), authorities, userDetails.isEnabled()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{username}/validate-password")
    public ResponseEntity<?> validateCredentials(@PathVariable("username") String username, @RequestBody PasswordDto dto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, dto.getPassword()));
            return ResponseEntity.noContent().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
