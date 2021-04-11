package ru.dipech.listeneng.repository;


import org.springframework.data.repository.Repository;
import ru.dipech.listeneng.entity.persistence.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends Repository<UserRole, Long> {

    Optional<UserRole> findByName(String name);

}
