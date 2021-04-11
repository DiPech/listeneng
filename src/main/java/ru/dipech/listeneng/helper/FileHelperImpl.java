package ru.dipech.listeneng.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.FileScope;
import ru.dipech.listeneng.service.FileService;
import ru.dipech.listeneng.util.Constants;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileHelperImpl implements FileHelper {

    private final FileService fileService;

    @Override
    public Path create(InputStream stream) {
        var scope = FileScope.PUBLIC;
        var fileName = UUID.randomUUID().toString() + ".mp3";
        if (fileService.has(fileName, scope)) {
            fileService.delete(fileName, scope);
        }
        fileService.put(fileName, scope, stream, Constants.HOUR);
        return fileService.get(fileName, scope);
    }

}
