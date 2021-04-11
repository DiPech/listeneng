package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.Locale;
import ru.dipech.listeneng.exception.ApplicationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

    private static final String REGEX_RU = "[^а-яА-ЯёЁ]";
    private static final String REGEX_EN = "[^a-zA-Z]";

    @Override
    public Locale detect(String text) {
        String onlyRuSymbols = text.replaceAll(REGEX_RU, "");
        String onlyEnSymbols = text.replaceAll(REGEX_EN, "");
        if (onlyRuSymbols.length() >= onlyEnSymbols.length()) {
            return Locale.RU;
        }
        return Locale.EN;
    }

    @Override
    public Locale parse(String code) {
        var optionalLocale = Locale.LOCALES.stream()
            .filter(locale -> locale.getCode().equals(code))
            .findFirst();
        if (optionalLocale.isEmpty()) {
            throw new ApplicationException("Locale not found");
        }
        return optionalLocale.get();
    }

}
