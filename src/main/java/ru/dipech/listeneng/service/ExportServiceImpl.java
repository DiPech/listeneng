package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.FileScope;
import ru.dipech.listeneng.entity.dto.export.EntryDto;
import ru.dipech.listeneng.entity.dto.export.SectionDto;
import ru.dipech.listeneng.entity.dto.export.WrapperDto;
import ru.dipech.listeneng.entity.persistence.Entry;
import ru.dipech.listeneng.entity.persistence.Section;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.repository.SectionRepository;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.dipech.listeneng.util.ThrowingSupplier.throwingSupplier;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    private static final DateFormat DATE_TIME_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT);

    private final SectionRepository sectionRepository;
    private final JsonService jsonService;
    private final FileService fileService;

    @Override
    @Transactional
    public Path exportAll(User user) {
        var sections = sectionRepository.findAllByUserAndParentIsNull(user);
        var sectionDtos = convertSectionsToDto(sections);
        var data = new WrapperDto(sectionDtos);
        var objectMapper = jsonService.providerObjectMapper();
        var json = throwingSupplier(() -> objectMapper.writeValueAsString(data));
        var inputStream = new ByteArrayInputStream(json.getBytes());
        var currentDateTime = DATE_TIME_FORMATTER.format(new Date());
        var fileName = "listeneng-data-" + currentDateTime + ".json";
        fileService.put(fileName, FileScope.PUBLIC, inputStream, 600);
        return fileService.get(fileName, FileScope.PUBLIC);
    }

    private List<SectionDto> convertSectionsToDto(List<Section> sections) {
        return sections.stream().map(this::convertSectionsToDto).collect(Collectors.toList());
    }

    private SectionDto convertSectionsToDto(Section section) {
        return SectionDto.builder()
            .name(section.getName())
            .entries(convertEntriesToDto(section.getEntries()))
            .children(convertSectionsToDto(section.getChildren()))
            .build();
    }

    private List<EntryDto> convertEntriesToDto(List<Entry> entries) {
        return entries.stream().map(this::convertEntriesToDto).collect(Collectors.toList());
    }

    private EntryDto convertEntriesToDto(Entry entry) {
        return EntryDto.builder()
            .type(entry.getType().toString())
            .text(entry.getText())
            .phrase(entry.getPhrase())
            .translation(entry.getTranslation())
            .divider(entry.getDivider())
            .build();
    }

}
