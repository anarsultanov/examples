package dev.sultanov.keycloak.server.provider.dto;

public class PasswordDto {

    private String password;

    public PasswordDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
