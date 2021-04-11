package ru.dipech.listeneng.exception;

import ru.dipech.listeneng.util.StringUtils;

public class ApplicationException extends RuntimeException {

    public ApplicationException(String template, Object... args) {
        super(StringUtils.compile(template, args));
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }

}
