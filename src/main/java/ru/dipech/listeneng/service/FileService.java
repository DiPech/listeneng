package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.FileScope;

import java.io.InputStream;
import java.nio.file.Path;

public interface FileService {

    boolean has(String fileName, FileScope fileScope);

    Path get(String fileName, FileScope fileScope);

    void put(String fileName, FileScope fileScope, InputStream inputStream, int lifetimeInSeconds);

    void delete(String fileName, FileScope fileScope);

}
