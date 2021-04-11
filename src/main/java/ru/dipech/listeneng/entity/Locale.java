package ru.dipech.listeneng.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Locale {

    public static final Locale RU = new Locale("ru-RU");
    public static final Locale EN = new Locale("en-US");

    public static final List<Locale> LOCALES = List.of(RU, EN);

    private final String code;
}
