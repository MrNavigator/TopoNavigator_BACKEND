package pl.toponavigator.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleEnum {
    USER(0),
    ADMIN(10);
    private final int permissionLevel;

    public int getPermissionLevel() {
        return this.permissionLevel;
    }
}
