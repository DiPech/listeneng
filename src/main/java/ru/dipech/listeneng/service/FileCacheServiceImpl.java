package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.FileScope;
import ru.dipech.listeneng.repository.FileRepository;

import java.io.InputStream;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileCacheServiceImpl implements FileCacheService {

    private final FileService fileService;
    private final FileRepository fileRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean has(String fileName) {
        return fileService.has(fileName, FileScope.CACHE);
    }

    @Override
    @Transactional(readOnly = true)
    public Path get(String fileName) {
        return fileService.get(fileName, FileScope.CACHE);
    }

    @Override
    @Transactional
    public void put(String fileName, InputStream inputStream, int lifetimeInSeconds) {
        fileService.put(fileName, FileScope.CACHE, inputStream, lifetimeInSeconds);
    }

    @Override
    @Transactional
    public void deleteAll() {
        var files = fileRepository.findAllByFileScope(FileScope.CACHE);
        files.forEach(file -> fileService.delete(file.getFileName(), file.getFileScope()));
        fileRepository.deleteAll(files);
    }

}
