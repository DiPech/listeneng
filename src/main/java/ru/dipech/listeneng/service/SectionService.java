package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.dto.SectionListDto;
import ru.dipech.listeneng.entity.dto.SectionTreeDto;
import ru.dipech.listeneng.entity.persistence.Section;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.List;
import java.util.UUID;

public interface SectionService {

    List<Section> getRoot(User user);

    List<Section> getByIds(User user, List<UUID> ids);

    void sync(User user, List<Section> sections);

    void syncSections(User user, List<SectionTreeDto> sections);

    void syncEntries(User user, List<SectionListDto> sections);

    void update(User user, List<Section> sections);

    void delete(User user, List<UUID> ids);

    void updateGlobalPriorities(User user);

}
