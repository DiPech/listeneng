package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.persistence.Entry;
import ru.dipech.listeneng.entity.persistence.EntryPart;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.NotFoundException;
import ru.dipech.listeneng.repository.EntryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntryTranslationServiceImpl implements EntryTranslationService {

    private final EntryRepository entryRepository;
    private final TextTranslationService textTranslationService;

    @Override
    public String translate(User user, UUID entryId, EntryPart entryPart) {
        var optionalEntry = entryRepository.findBySectionUserAndId(user, entryId);
        if (optionalEntry.isEmpty()) {
            throw new NotFoundException("entry");
        }
        var entry = optionalEntry.get();
        return textTranslationService.translate(detectText(entry, entryPart));
    }

    private String detectText(Entry entry, EntryPart entryPart) {
        if (entryPart == EntryPart.PHRASE) {
            return entry.getPhrase();
        }
        if (entryPart == EntryPart.TRANSLATION) {
            return entry.getTranslation();
        }
        throw new RuntimeException("Cannot determine entry part type");
    }

}
