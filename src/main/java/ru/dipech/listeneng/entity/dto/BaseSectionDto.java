package ru.dipech.listeneng.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseSectionDto {

    private String id;
    private String name;

    public BaseSectionDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
