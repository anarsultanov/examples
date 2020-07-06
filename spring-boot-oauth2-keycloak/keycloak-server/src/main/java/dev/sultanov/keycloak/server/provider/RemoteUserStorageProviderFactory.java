package dev.sultanov.keycloak.server.provider;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

public class RemoteUserStorageProviderFactory implements UserStorageProviderFactory<RemoteUserStorageProvider> {

    public static final String PROVIDER_NAME = "remote";
    public static final String USER_SERVICE_URI = "userServiceUri";

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }

    @Override
    public RemoteUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        String uri = componentModel.getConfig().getFirst(USER_SERVICE_URI);
        return new RemoteUserStorageProvider(keycloakSession, componentModel, buildClient(uri));
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        ProviderConfigProperty userServiceUrl = new ProviderConfigProperty(
                USER_SERVICE_URI,
                "URI",
                "Remote user service URI.",
                ProviderConfigProperty.STRING_TYPE,
                "http://localhost:8080"
        );
        return List.of(userServiceUrl);
    }

    private static RemoteUserService buildClient(String uri) {
        ResteasyClient client = new ResteasyClientBuilder().disableTrustManager().build();
        ResteasyWebTarget target = client.target(uri);
        return target
                .proxyBuilder(RemoteUserService.class)
                .classloader(RemoteUserService.class.getClassLoader())
                .build();
    }
}
