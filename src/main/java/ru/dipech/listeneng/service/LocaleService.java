package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.Locale;

public interface LocaleService {

    Locale detect(String text);

    Locale parse(String code);

}
