package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.repository.FileRepository;
import ru.dipech.listeneng.util.Constants;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class OldFileRemoverServiceImpl implements OldFileRemoverService {

    private final FileRepository fileRepository;
    private final FileService fileService;

    @Override
    @Transactional
    @Scheduled(initialDelay = 10000, fixedDelay = Constants.MINUTE * 1000)
    public void remove() {
        var filesToRemove = fileRepository.findAllByExpiresAtIsLessThan(new Date());
        filesToRemove.forEach(file -> {
            try {
                fileService.delete(file.getFileName(), file.getFileScope());
            } catch (Exception exception) {
                log.error("Couldn't remove a file with name {} and scope {}", file.getFileName(), file.getFileScope());
            }
        });
        fileRepository.deleteAll(filesToRemove);
    }

}
