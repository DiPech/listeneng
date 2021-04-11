package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.dto.NewUserDto;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.UUID;

public interface UserService {

    User getByEmail(String email);

    User getById(UUID id);

    void addUser(NewUserDto userDto);

}
