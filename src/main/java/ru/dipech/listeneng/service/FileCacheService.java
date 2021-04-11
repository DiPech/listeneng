package ru.dipech.listeneng.service;

import java.io.InputStream;
import java.nio.file.Path;

public interface FileCacheService {

    boolean has(String fileName);

    Path get(String fileName);

    void put(String fileName, InputStream inputStream, int lifetimeInSeconds);

    void deleteAll();

}
