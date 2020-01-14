package dev.sultanov.springboot.oauth2.pkce.config.pkce;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.HashMap;
import java.util.Map;

public class PkceAuthorizationCodeTokenGranter extends AuthorizationCodeTokenGranter {

    private final PkceAuthorizationCodeServices authorizationCodeServices;

    public PkceAuthorizationCodeTokenGranter(AuthorizationServerTokenServices tokenServices, PkceAuthorizationCodeServices authorizationCodeServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory);
        this.authorizationCodeServices = authorizationCodeServices;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        String authorizationCode = parameters.get("code");
        String redirectUri = parameters.get("redirect_uri");
        if (authorizationCode == null) {
            throw new InvalidRequestException("An authorization code must be supplied.");
        } else {
            String codeVerifier = parameters.getOrDefault("code_verifier", "");
            OAuth2Authentication storedAuth = authorizationCodeServices.consumeAuthorizationCodeAndCodeVerifier(authorizationCode, codeVerifier);
            if (storedAuth == null) {
                throw new InvalidGrantException("Invalid authorization code: " + authorizationCode);
            } else {
                OAuth2Request pendingOAuth2Request = storedAuth.getOAuth2Request();
                String redirectUriApprovalParameter = pendingOAuth2Request.getRequestParameters().get("redirect_uri");
                if ((redirectUri != null || redirectUriApprovalParameter != null) && !pendingOAuth2Request.getRedirectUri().equals(redirectUri)) {
                    throw new RedirectMismatchException("Redirect URI mismatch.");
                } else {
                    String pendingClientId = pendingOAuth2Request.getClientId();
                    String clientId = tokenRequest.getClientId();
                    if (clientId != null && !clientId.equals(pendingClientId)) {
                        throw new InvalidClientException("Client ID mismatch");
                    } else {
                        Map<String, String> combinedParameters = new HashMap<>(pendingOAuth2Request.getRequestParameters());
                        combinedParameters.putAll(parameters);
                        OAuth2Request finalStoredOAuth2Request = pendingOAuth2Request.createOAuth2Request(combinedParameters);
                        Authentication userAuth = storedAuth.getUserAuthentication();
                        return new OAuth2Authentication(finalStoredOAuth2Request, userAuth);
                    }
                }
            }
        }
    }
}
