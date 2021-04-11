package ru.dipech.listeneng.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

}
