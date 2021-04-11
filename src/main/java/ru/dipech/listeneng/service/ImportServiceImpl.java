package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.dto.export.EntryDto;
import ru.dipech.listeneng.entity.dto.export.SectionDto;
import ru.dipech.listeneng.entity.dto.export.WrapperDto;
import ru.dipech.listeneng.entity.persistence.Entry;
import ru.dipech.listeneng.entity.persistence.EntryType;
import ru.dipech.listeneng.entity.persistence.Section;
import ru.dipech.listeneng.entity.persistence.User;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.dipech.listeneng.util.ThrowingSupplier.throwingSupplier;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final SectionService sectionService;
    private final JsonService jsonService;

    private User user;

    @Override
    @Transactional
    public void importAll(User user, InputStream stream) {
        this.user = user;
        var objectMapper = jsonService.providerObjectMapper();
        var data = throwingSupplier(() -> objectMapper.readValue(stream, WrapperDto.class));
        var sections = convertSectionsFromDto(data.getSections(), null);
        sectionService.sync(user, sections);
    }

    private List<Section> convertSectionsFromDto(List<SectionDto> sectionDtos, Section parent) {
        var priority = new AtomicInteger(0);
        return sectionDtos.stream()
            .map(sectionDto -> convertSectionFromDto(sectionDto, priority.getAndIncrement(), parent))
            .collect(Collectors.toList());
    }

    private Section convertSectionFromDto(SectionDto sectionDto, int priority, Section parent) {
        var section = new Section();
        return section
            .setUser(user)
            .setPriority(priority)
            .setGlobalPriority(0)
            .setName(sectionDto.getName())
            .setParent(parent)
            .setChildren(convertSectionsFromDto(sectionDto.getChildren(), section))
            .setEntries(convertEntriesFromDto(sectionDto.getEntries(), section));
    }

    private List<Entry> convertEntriesFromDto(List<EntryDto> entries, Section section) {
        var priority = new AtomicInteger(0);
        return entries.stream()
            .map(entryDto -> convertEntryFromDto(entryDto, priority.getAndIncrement(), section))
            .collect(Collectors.toList());
    }

    private Entry convertEntryFromDto(EntryDto entry, int priority, Section section) {
        return new Entry()
            .setSection(section)
            .setPriority(priority)
            .setType(EntryType.valueOf(entry.getType()))
            .setText(entry.getText())
            .setPhrase(entry.getPhrase())
            .setTranslation(entry.getTranslation())
            .setDivider(entry.getDivider());
    }

}
