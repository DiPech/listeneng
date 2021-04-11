package ru.dipech.listeneng.entity.persistence;

import static ru.dipech.listeneng.service.UserRoleService.ROLE_PREFIX;

public class Permission {

    public static final String ADMIN = ROLE_PREFIX + UserRole.ROLE_ADMIN;
    public static final String USER = ROLE_PREFIX + UserRole.ROLE_USER;

}
