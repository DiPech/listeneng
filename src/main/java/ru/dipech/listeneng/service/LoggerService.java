package ru.dipech.listeneng.service;

import org.slf4j.Logger;
import ru.dipech.listeneng.exception.NotInitializedException;

public interface LoggerService {

    void init(Logger logger);

    void init(Logger logger, String scope);

    void log(String template, Object... objects) throws NotInitializedException;

}
