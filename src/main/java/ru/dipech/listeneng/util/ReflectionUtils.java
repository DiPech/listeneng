package ru.dipech.listeneng.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Character.toUpperCase;
import static ru.dipech.listeneng.util.ThrowingSupplier.throwingSupplier;

public class ReflectionUtils {

    public static List<Method> getMethods(Object object) {
        return Arrays.asList(object.getClass().getDeclaredMethods());
    }

    public static List<Method> getGetters(Object object) {
        return getMethods(object).stream()
            .filter(method -> method.getName().startsWith("get"))
            .collect(Collectors.toList());
    }

    public static Method getMethod(Object object, String method) {
        return throwingSupplier(() -> object.getClass().getMethod(method));
    }

    public static Method getGetter(Object object, String field) {
        return getMethod(object, getGetterName(field));
    }

    public static Object invokeMethod(Object object, Method method, Object... args) {
        return throwingSupplier(() -> method.invoke(object, args));
    }

    public static Object invokeGetter(Object object, String field) {
        Method getter = getGetter(object, field);
        return invokeMethod(object, getter);
    }

    private static String getGetterName(String field) {
        return "get" + toUpperCase(field.charAt(0)) + field.substring(1);
    }

}
