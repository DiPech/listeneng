package ru.dipech.listeneng.entity.dto.export;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SectionDto {

    private String name;
    private List<EntryDto> entries;
    private List<SectionDto> children;

    @Builder
    public SectionDto(String name, List<EntryDto> entries, List<SectionDto> children) {
        this.name = name;
        this.entries = entries;
        this.children = children;
    }

}
