package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.entity.persistence.UserRole;
import ru.dipech.listeneng.exception.NotFoundException;
import ru.dipech.listeneng.repository.UserRoleRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    public static final String ROLE_PREFIX = "ROLE_";

    private final UserRoleRepository userRoleRepository;

    @Transactional(readOnly = true)
    public UserRole getUserRole(String role) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findByName(getRoleName(role));
        if (optionalUserRole.isEmpty()) {
            throw new NotFoundException("UserRole");
        }
        return optionalUserRole.get();
    }

    @Transactional(readOnly = true)
    public Set<UserRole> getUserRoles(String... roles) {
        return Stream.of(roles)
            .map(this::getUserRole)
            .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public boolean isUserHasRole(User user, String role) {
        return user.getRoles().stream()
            .map(UserRole::getName)
            .collect(Collectors.toSet())
            .contains(getRoleName(role));
    }

    private String getRoleName(String role) {
        return ROLE_PREFIX + role;
    }

}
