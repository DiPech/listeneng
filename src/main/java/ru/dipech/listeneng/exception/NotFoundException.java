package ru.dipech.listeneng.exception;

import ru.dipech.listeneng.util.StringUtils;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String template, Object... args) {
        super("{} not found", StringUtils.compile(template, args));
    }

}
