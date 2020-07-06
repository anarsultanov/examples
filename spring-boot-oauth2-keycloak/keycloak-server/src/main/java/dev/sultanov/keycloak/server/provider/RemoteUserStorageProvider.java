package dev.sultanov.keycloak.server.provider;

import dev.sultanov.keycloak.server.provider.dto.PasswordDto;
import dev.sultanov.keycloak.server.provider.dto.UserDetailsDto;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class RemoteUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final RemoteUserService userService;

    public RemoteUserStorageProvider(KeycloakSession session, ComponentModel model, RemoteUserService userService) {
        this.session = session;
        this.model = model;
        this.userService = userService;
    }

    @Override
    public void close() {
        // noop
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        try {
            UserDetailsDto userDetails = userService.getUserDetails(username);

            UserModel userModel = session.userLocalStorage().addUser(realm, username);
            userModel.setFederationLink(model.getId());
            userModel.setEnabled(userDetails.isEnabled());

            if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
                for (String authority : userDetails.getAuthorities()) {
                    RoleModel roleModel = realm.getRole(authority);
                    if (roleModel != null) {
                        userModel.grantRole(roleModel);
                    }
                }
            }

            return userModel;
        } catch (NotFoundException e) {
            return null;
        }
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return null;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        if (!supportsCredentialType(input.getType())) return false;

        Response response = userService.validateLogin(user.getUsername(), new PasswordDto(input.getChallengeResponse()));
        boolean valid = response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL;

        if (valid) {
            user.setFederationLink(null);
            session.userCredentialManager().updateCredential(realm, user, input);
        }

        return valid;
    }
}
