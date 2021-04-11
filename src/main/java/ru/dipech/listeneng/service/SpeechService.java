package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.Locale;

import java.io.InputStream;

public interface SpeechService {

    InputStream speech(String text, Locale locale);

}
