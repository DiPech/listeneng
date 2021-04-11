package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.Locale;
import ru.dipech.listeneng.util.Constants;

import java.io.InputStream;

import static java.nio.file.Files.newInputStream;
import static ru.dipech.listeneng.util.ThrowingSupplier.throwingSupplier;

@Service
@Primary
@RequiredArgsConstructor
public class SpeechCachingServiceImpl implements SpeechService {

    private final static int MAX_TEXT_LENGTH = 100;

    @Qualifier("main")
    private final SpeechService speechService;

    private final SlugService slugService;
    private final FileCacheService fileCacheService;

    @Override
    public InputStream speech(String text, Locale locale) {
        boolean isShort = text.length() < MAX_TEXT_LENGTH;
        String finalText = isShort ? text : text.substring(0, MAX_TEXT_LENGTH);
        String cacheFileName = slugService.slugify(finalText) + ".mp3";
        if (fileCacheService.has(cacheFileName)) {
            return throwingSupplier(() -> newInputStream(fileCacheService.get(cacheFileName)));
        }
        var result = speechService.speech(text, locale);
        fileCacheService.put(cacheFileName, result, isShort ? Constants.MONTH : Constants.HOUR);
        return throwingSupplier(() -> newInputStream(fileCacheService.get(cacheFileName)));
    }

}
