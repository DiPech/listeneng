package ru.dipech.listeneng.service;

import java.nio.file.Path;

public interface TextSpeechService {

    Path speech(String text);

}
