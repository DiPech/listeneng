package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.dto.SectionListDto;
import ru.dipech.listeneng.entity.dto.SectionTreeDto;
import ru.dipech.listeneng.entity.persistence.BaseEntity;
import ru.dipech.listeneng.entity.persistence.Entry;
import ru.dipech.listeneng.entity.persistence.Section;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.ApplicationException;
import ru.dipech.listeneng.repository.EntryRepository;
import ru.dipech.listeneng.repository.SectionRepository;
import ru.dipech.listeneng.service.validator.Validator;

import java.util.*;
import java.util.stream.Collectors;

import static ru.dipech.listeneng.util.CollectionUtils.*;

@Service
@RequiredArgsConstructor
@Qualifier("main")
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;
    private final EntryRepository entryRepository;
    private final Validator<Section> sectionValidator;
    private final Validator<Entry> entryValidator;
    private final SectionDtoService sectionDtoService;
    private final FetchService fetchService;

    private int globalPriorityCounter;

    @Override
    @Transactional(readOnly = true)
    public List<Section> getRoot(User user) {
        List<Section> result = sectionRepository.findAllByUserAndParentIsNull(user);
        Collections.sort(result);
        fetchService.fetch(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Section> getByIds(User user, List<UUID> ids) {
        List<Section> sections = sectionRepository.findAllByUserAndIdInOrderByGlobalPriority(user, ids);
        fetchService.fetch(sections);
        return sections;
    }

    @Override
    @Transactional
    public void sync(User user, List<Section> sections) {
        sectionRepository.deleteAll(sectionRepository.findAllByUser(user));
        update(user, sections);
        updateGlobalPriorities(user);
    }

    @Override
    @Transactional
    public void syncSections(User user, List<SectionTreeDto> sectionDtos) {
        sectionDtoService.init(user);
        List<Section> allSections = sectionRepository.findAllByUser(user);
        List<Section> sections = sectionDtoService.getFromTreeDto(sectionDtos);
        update(user, sections);
        List<Section> deletedSections = detectDeletedSections(allSections, sectionDtos);
        List<UUID> deletedSectionsIds = deletedSections.stream()
            .map(BaseEntity::getId)
            .collect(Collectors.toList());
        delete(user, deletedSectionsIds);
        updateGlobalPriorities(user);
    }

    @Override
    @Transactional
    public void syncEntries(User user, List<SectionListDto> sectionDtos) {
        sectionDtoService.init(user);
        var tmpSections = sectionDtoService.getFromListDto(sectionDtos);
        var sections = new ArrayList<Section>();
        tmpSections.forEach(tmpSection -> {
            var sectionOptional = sectionRepository.findFirstByUserAndId(user, tmpSection.getId());
            if (sectionOptional.isEmpty()) {
                throw new ApplicationException("Section with id {} is not found", tmpSection.getId());
            }
            var section = sectionOptional.get();
            entryRepository.deleteAll(section.getEntries());
            section.getEntries().clear();
            section.getEntries().addAll(tmpSection.getEntries());
            sections.add(section);
        });
        var entries = sections.stream()
            .flatMap(section -> section.getEntries().stream())
            .collect(Collectors.toList());
        entryValidator.validate(entries);
        sectionRepository.saveAll(sections);
    }

    @Override
    @Transactional
    public void update(User user, List<Section> sections) {
        sectionValidator.validate(flattenTree(sections));
        sectionRepository.saveAll(sections);
    }

    @Override
    @Transactional
    public void delete(User user, List<UUID> ids) {
        List<Section> sectionsToDelete = sectionRepository.findAllByUserAndIdIn(user, ids);
        sectionRepository.deleteAll(sectionsToDelete);
        createRootSectionIfNoSectionsExist(user);
    }

    @Override
    @Transactional
    public void updateGlobalPriorities(User user) {
        List<Section> rootSections = sectionRepository.findAllByUserAndParentIsNull(user);
        globalPriorityCounter = 0;
        doUpdateGlobalPriorities(rootSections);
        sectionRepository.saveAll(rootSections);
    }

    private void doUpdateGlobalPriorities(List<Section> sections) {
        for (var section : sections) {
            section.setGlobalPriority(globalPriorityCounter++);
            doUpdateGlobalPriorities(section.getChildren());
        }
    }

    private List<Section> detectDeletedSections(List<Section> sections, List<SectionTreeDto> sectionDtos) {
        Set<UUID> realIds = sections.stream()
            .map(BaseEntity::getId)
            .collect(Collectors.toSet());
        List<String> givenIdsStr = collectIds(sectionDtos);
        Set<UUID> givenIds = givenIdsStr.stream()
            .map(UUID::fromString)
            .collect(Collectors.toSet());
        Set<UUID> idsToDelete = findDifference(realIds, givenIds);
        Map<UUID, Section> idToSection = listToMap(sections, BaseEntity::getId);
        idsToDelete.forEach(id -> sectionRepository.delete(idToSection.get(id)));
        return new ArrayList<>(extractValuesByKeys(idToSection, idsToDelete));
    }

    private void createRootSectionIfNoSectionsExist(User user) {
        if (sectionRepository.count() > 0) {
            return;
        }
        var rootSection = new Section();
        rootSection.setName("All sections");
        rootSection.setPriority(0);
        rootSection.setGlobalPriority(0);
        rootSection.setUser(user);
        sectionRepository.save(rootSection);
    }

}
