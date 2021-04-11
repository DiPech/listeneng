package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.helper.FileHelper;

import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextSpeechServiceImpl implements TextSpeechService {

    private final LocaleService localeService;
    private final SpeechService speechService;
    private final FileHelper fileHelper;

    @Override
    public Path speech(String text) {
        return fileHelper.create(speechService.speech(text, localeService.detect(text)));
    }

}
