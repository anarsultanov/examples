package dev.sultanov.springboot.oauth2.pkce.config.pkce;

import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class PkceProtectedAuthentication {
    private final String codeChallenge;
    private final CodeChallengeMethod codeChallengeMethod;
    private final OAuth2Authentication authentication;

    public PkceProtectedAuthentication(OAuth2Authentication authentication) {
        this.codeChallenge = null;
        this.codeChallengeMethod = CodeChallengeMethod.NONE;
        this.authentication = authentication;
    }

    public PkceProtectedAuthentication(String codeChallenge, CodeChallengeMethod codeChallengeMethod, OAuth2Authentication authentication) {
        this.codeChallenge = codeChallenge;
        this.codeChallengeMethod = codeChallengeMethod;
        this.authentication = authentication;
    }

    public OAuth2Authentication validateAndGetAuthentication(String codeVerifier) {
        if (codeChallengeMethod.validate(codeChallenge, codeVerifier)) {
            return authentication;
        } else {
            throw new InvalidGrantException("Invalid code verifier.");
        }
    }
}
