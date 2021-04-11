package ru.dipech.listeneng.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SectionListDto extends BaseSectionDto {

    private List<String> fullName;
    private Boolean isFinal;
    private List<EntryDto> entries;

    @Builder
    public SectionListDto(String id, String name, List<String> fullName, Boolean isFinal, List<EntryDto> entries) {
        super(id, name);
        this.fullName = fullName;
        this.isFinal = isFinal;
        this.entries = entries;
    }

}
