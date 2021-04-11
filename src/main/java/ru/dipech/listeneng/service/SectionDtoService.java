package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.dto.SectionListDto;
import ru.dipech.listeneng.entity.dto.SectionTreeDto;
import ru.dipech.listeneng.entity.persistence.Section;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.NotInitializedException;

import java.util.List;

public interface SectionDtoService {

    void init(User user);

    List<SectionTreeDto> convertToTreeDto(List<Section> sections) throws NotInitializedException;

    List<SectionListDto> convertToListDto(List<Section> sections) throws NotInitializedException;

    List<Section> getFromTreeDto(List<SectionTreeDto> sectionDtos) throws NotInitializedException;

    List<Section> getFromListDto(List<SectionListDto> sectionDtos) throws NotInitializedException;

}
