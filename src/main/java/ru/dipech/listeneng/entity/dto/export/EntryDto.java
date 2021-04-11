package ru.dipech.listeneng.entity.dto.export;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dipech.listeneng.entity.dto.BaseEntryDto;

@Getter
@Setter
@NoArgsConstructor
public class EntryDto extends BaseEntryDto {

    @Builder
    public EntryDto(String type, String text, String phrase, String translation, String divider) {
        super(type, text, phrase, translation, divider);
    }

}
