package ru.dipech.listeneng.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dipech.listeneng.entity.persistence.TreeEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SectionTreeDto extends BaseSectionDto implements TreeEntity<SectionTreeDto, String> {

    private Boolean isNew;
    private List<SectionTreeDto> children;
    private Integer entriesCount;
    private Integer phrasesCount;

    @Builder
    public SectionTreeDto(String id, String name, Boolean isNew, List<SectionTreeDto> children, Integer entriesCount,
                          Integer phrasesCount) {
        super(id, name);
        this.isNew = isNew;
        this.children = children;
        this.entriesCount = entriesCount;
        this.phrasesCount = phrasesCount;
    }

}
