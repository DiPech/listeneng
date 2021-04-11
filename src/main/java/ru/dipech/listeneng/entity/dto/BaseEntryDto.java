package ru.dipech.listeneng.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntryDto {

    private String type; // EntryType
    private String text;
    private String phrase;
    private String translation;
    private String divider;

    public BaseEntryDto(String type, String text, String phrase, String translation, String divider) {
        this.type = type;
        this.text = text;
        this.phrase = phrase;
        this.translation = translation;
        this.divider = divider;
    }

}
