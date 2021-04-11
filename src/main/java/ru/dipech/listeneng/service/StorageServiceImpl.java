package ru.dipech.listeneng.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static ru.dipech.listeneng.util.ThrowingRunnable.throwingRunnable;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    private final Path storagePath;

    public StorageServiceImpl(
        @Value("${app.storage}") String storagePath
    ) {
        this.storagePath = Paths.get(storagePath);
    }

    @Override
    public Path getStoragePath() {
        return storagePath;
    }

    @Override
    public boolean has(String relativePath) {
        return Files.exists(get(relativePath));
    }

    @Override
    public Path get(String relativePath) {
        return storagePath.resolve(relativePath);
    }

    @Override
    public void put(String relativePath, InputStream inputStream) {
        throwingRunnable(() -> copyInputStreamToFile(inputStream, get(relativePath).toFile()));
    }

    @Override
    public void delete(String relativePath) {
        throwingRunnable(() -> {
            var path = get(relativePath);
            if (!Files.exists(path)) {
                log.warn("Attempt to delete not existent file {}", path);
                return;
            }
            Files.delete(path);
        });
    }

}
