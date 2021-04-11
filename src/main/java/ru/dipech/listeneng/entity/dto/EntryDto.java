package ru.dipech.listeneng.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EntryDto extends BaseEntryDto {

    private String id;

    @Builder
    public EntryDto(String type, String text, String phrase, String translation, String divider, String id) {
        super(type, text, phrase, translation, divider);
        this.id = id;
    }

}
