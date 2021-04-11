package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.FileScope;
import ru.dipech.listeneng.entity.persistence.File;
import ru.dipech.listeneng.exception.ApplicationException;
import ru.dipech.listeneng.repository.FileRepository;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final StorageService storageService;
    private final FileRepository fileRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean has(String fileName, FileScope fileScope) {
        var optionalFile = fileRepository.findByFileNameAndFileScope(fileName, fileScope);
        if (optionalFile.isEmpty()) {
            return false;
        }
        return storageService.has(getRelativePath(fileName, fileScope));
    }

    @Override
    @Transactional(readOnly = true)
    public Path get(String fileName, FileScope fileScope) {
        var optionalFile = fileRepository.findByFileNameAndFileScope(fileName, fileScope);
        if (optionalFile.isEmpty()) {
            throw new ApplicationException("Cannot find such file");
        }
        return storageService.get(getRelativePath(fileName, fileScope));
    }

    @Override
    @Transactional
    public void put(String fileName, FileScope fileScope, InputStream inputStream, int lifetimeInSeconds) {
        storageService.put(getRelativePath(fileName, fileScope), inputStream);
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, lifetimeInSeconds);
        var file = new File().setFileName(fileName).setFileScope(fileScope).setExpiresAt(calendar.getTime());
        fileRepository.save(file);
    }

    @Override
    @Transactional
    public void delete(String fileName, FileScope fileScope) {
        storageService.delete(getRelativePath(fileName, fileScope));
        var optionalFile = fileRepository.findByFileNameAndFileScope(fileName, fileScope);
        optionalFile.ifPresent(fileRepository::delete);
    }

    private String getRelativePath(String fileName, FileScope fileScope) {
        return fileScope.toString().toLowerCase() + "/" + fileName;
    }

}
