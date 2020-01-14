package dev.sultanov.springboot.oauth2.pkce.config;

import dev.sultanov.springboot.oauth2.pkce.config.pkce.PkceAuthorizationCodeServices;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public AuthServerConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authorizationCodeServices(new PkceAuthorizationCodeServices(endpoints.getClientDetailsService(), passwordEncoder));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("public")
                .secret("{noop}")
                .redirectUris("http://localhost:8080/")
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .autoApprove(true)
                .and()
                .withClient("private")
                .secret("{noop}secret")
                .redirectUris("http://localhost:8080/")
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .autoApprove(true);
    }
}
