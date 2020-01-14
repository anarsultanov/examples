package dev.sultanov.springboot.oauth2.pkce.config.pkce;

import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public enum CodeChallengeMethod {
    S256 {
        @Override
        public boolean validate(String codeChallenge, String codeVerifier) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
                return codeChallenge.equals(Base64.getUrlEncoder().encodeToString(Hex.encode(hash)));
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
        }
    },
    PLAIN {
        @Override
        public boolean validate(String codeChallenge, String codeVerifier) {
            return codeChallenge.equals(codeVerifier);
        }
    };

    public abstract boolean validate(String codeChallenge, String codeVerifier);
}
