package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.persistence.User;

import java.nio.file.Path;

public interface ExportService {

    Path exportAll(User user);

}
