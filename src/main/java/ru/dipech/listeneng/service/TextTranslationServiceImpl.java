package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextTranslationServiceImpl implements TextTranslationService {

    private final TranslationService translationService;
    private final LocaleService localeService;

    @Override
    public String translate(String text) {
        var localeInput = detectLocaleInput(text);
        var localeOutput = detectLocaleOutput(localeInput);
        return translationService.translate(text, localeInput, localeOutput);
    }

    private Locale detectLocaleInput(String text) {
        return localeService.detect(text);
    }

    private Locale detectLocaleOutput(Locale inputLocale) {
        if (inputLocale == Locale.EN) {
            return Locale.RU;
        }
        if (inputLocale == Locale.RU) {
            return Locale.EN;
        }
        throw new RuntimeException("Cannot determine locale");
    }

}
