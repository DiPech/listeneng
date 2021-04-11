package ru.dipech.listeneng.util;

public class StringUtils {

    public static String compile(String template, Object... args) {
        return String.format(template.replaceAll("\\{\\}", "%s"), args);
    }

}
