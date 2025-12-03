package com.miapp.compra.model;

public enum Role {
    ROLE_ADMIN("Administrador"),
    ROLE_USER("Usuario Registrado"),
    ROLE_GUEST("Invitado");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
