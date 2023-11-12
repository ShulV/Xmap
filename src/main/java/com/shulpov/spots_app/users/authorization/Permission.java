package com.shulpov.spots_app.users.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Shulpov Victor
 * @since 1.0
 * @version 1.0
 */
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete");

    @Getter
    private final String permission;
}

