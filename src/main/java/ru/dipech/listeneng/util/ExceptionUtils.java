package ru.dipech.listeneng.util;

import org.apache.commons.lang3.StringUtils;

public class ExceptionUtils {

    public static String getCleanedMessage(Throwable throwable) {
        Throwable rootCause = org.apache.commons.lang3.exception.ExceptionUtils.getRootCause(throwable);
        String message = rootCause.getMessage();
        String prefix = rootCause.getClass().getSimpleName() + ": ";
        return StringUtils.removeStart(message, prefix);
    }

}
