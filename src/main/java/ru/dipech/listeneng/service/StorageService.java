package ru.dipech.listeneng.service;

import java.io.InputStream;
import java.nio.file.Path;

public interface StorageService {

    Path getStoragePath();

    boolean has(String relativePath);

    Path get(String relativePath);

    void put(String relativePath, InputStream inputStream);

    void delete(String relativePath);

}
