package ru.fildv.groceryshop.database.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER, PROJECT_MANAGER, MANAGER, STOREKEEPER;

    @Override
    public String getAuthority() {
        return name();
    }
}
