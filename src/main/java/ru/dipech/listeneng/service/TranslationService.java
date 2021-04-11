package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.Locale;

public interface TranslationService {

    String translate(String text, Locale inputLocale, Locale outputLocale);

}
