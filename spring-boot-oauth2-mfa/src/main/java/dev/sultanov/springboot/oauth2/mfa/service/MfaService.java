package dev.sultanov.springboot.oauth2.mfa.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MfaService {

    private static final Map<String, String> SECRET_BY_USERNAME = Map.of("john", "JBSWY3DPEHPK3PXP");
    private GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();

    public boolean isEnabled(String username) {
        return SECRET_BY_USERNAME.containsKey(username);
    }

    public boolean verifyCode(String username, int code) {
        return code == googleAuthenticator.getTotpPassword(SECRET_BY_USERNAME.get(username));
    }
}
