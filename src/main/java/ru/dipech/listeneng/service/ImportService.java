package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.persistence.User;

import java.io.InputStream;

public interface ImportService {

    void importAll(User user, InputStream stream);

}
