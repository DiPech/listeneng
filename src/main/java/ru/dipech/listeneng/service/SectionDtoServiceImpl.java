package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.dto.EntryDto;
import ru.dipech.listeneng.entity.dto.SectionListDto;
import ru.dipech.listeneng.entity.dto.SectionTreeDto;
import ru.dipech.listeneng.entity.persistence.*;
import ru.dipech.listeneng.exception.NotInitializedException;
import ru.dipech.listeneng.repository.SectionRepository;

import java.util.*;
import java.util.stream.Collectors;

import static ru.dipech.listeneng.util.CollectionUtils.listToMap;

@Service
@RequiredArgsConstructor
public class SectionDtoServiceImpl implements SectionDtoService {

    private final SectionRepository sectionRepository;

    private User user;
    private Map<UUID, Section> idToSection;

    @Override
    @Transactional(readOnly = true)
    public void init(User user) {
        this.user = user;
        idToSection = listToMap(sectionRepository.findAllByUser(user), BaseEntity::getId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionTreeDto> convertToTreeDto(List<Section> sections) {
        ensureInitialized();
        return sections.stream()
            .map(this::convertToTreeDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SectionListDto> convertToListDto(List<Section> sections) {
        ensureInitialized();
        return sections.stream()
            .map(this::convertToListDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Section> getFromTreeDto(List<SectionTreeDto> sectionDtos) {
        ensureInitialized();
        return doGetFromTreeDto(sectionDtos, null);
    }

    private List<Section> doGetFromTreeDto(List<SectionTreeDto> sectionDtos, Section parent) {
        List<Section> sections = new ArrayList<>();
        int priority = 0;
        for (SectionTreeDto sectionDto : sectionDtos) {
            UUID givenId = UUID.fromString(sectionDto.getId());
            Section section;
            if (idToSection.containsKey(givenId)) {
                section = idToSection.get(givenId);
            } else {
                section = new Section().setGlobalPriority(0);
            }
            section
                .setPriority(priority++).setName(sectionDto.getName()).setUser(user)
                .setParent(parent).setChildren(doGetFromTreeDto(sectionDto.getChildren(), section));
            sections.add(section);
        }
        return sections;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Section> getFromListDto(List<SectionListDto> sectionDtos) {
        ensureInitialized();
        var sections = new ArrayList<Section>();
        sectionDtos.forEach(sectionDto -> {
            var sectionId = UUID.fromString(sectionDto.getId());
            var section = new Section();
            section.setId(sectionId);
            section.getEntries().addAll(getFromEntryDtos(sectionDto.getEntries(), section));
            sections.add(section);
        });
        return sections;
    }

    private List<Entry> getFromEntryDtos(List<EntryDto> entryDtos, Section section) {
        var priority = 0;
        var result = new ArrayList<Entry>();
        for (var entryDto : entryDtos) {
            result.add(getFromEntryDto(entryDto, section, priority++));
        }
        return result;
    }

    private Entry getFromEntryDto(EntryDto entryDto, Section section, int priority) {
        return new Entry()
            .setSection(section)
            .setPriority(priority)
            .setType(EntryType.valueOf(entryDto.getType()))
            .setText(entryDto.getText())
            .setDivider(entryDto.getDivider())
            .setPhrase(entryDto.getPhrase())
            .setTranslation(entryDto.getTranslation());
    }

    private SectionTreeDto convertToTreeDto(Section section) {
        ensureInitialized();
        int phrasesCount = (int) section.getEntries().stream()
            .filter(entry -> entry.getType() == EntryType.PHRASE)
            .count();
        assert section.getId() != null;
        String id = section.getId().toString();
        return SectionTreeDto.builder()
            .id(id)
            .isNew(false)
            .name(section.getName())
            .entriesCount(section.getEntries().size())
            .phrasesCount(phrasesCount)
            .children(convertToTreeDto(section.getChildren()))
            .build();
    }

    private SectionListDto convertToListDto(Section section) {
        ensureInitialized();
        assert section.getId() != null;
        return SectionListDto.builder()
            .id(section.getId().toString())
            .name(section.getName())
            .isFinal(section.getChildren().isEmpty())
            .fullName(getFullName(section))
            .entries(convertToEntryDtos(section.getEntries()))
            .build();
    }

    private List<EntryDto> convertToEntryDtos(List<Entry> entries) {
        return entries.stream()
            .map(this::convertToEntryDto)
            .collect(Collectors.toList());
    }

    private EntryDto convertToEntryDto(Entry entry) {
        assert entry.getId() != null;
        return EntryDto.builder()
            .id(entry.getId().toString())
            .type(entry.getType().toString())
            .text(entry.getText())
            .phrase(entry.getPhrase())
            .translation(entry.getTranslation())
            .divider(entry.getDivider())
            .build();
    }

    private List<String> getFullName(Section section) {
        Section current = section;
        List<String> result = new ArrayList<>();
        do {
            result.add(current.getName());
            current = current.getParent();
        } while (current != null);
        Collections.reverse(result);
        return result;
    }

    private void ensureInitialized() {
        if (user != null) {
            return;
        }
        throw new NotInitializedException();
    }

}
