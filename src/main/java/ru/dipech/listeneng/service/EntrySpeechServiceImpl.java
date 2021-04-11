package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.persistence.EntryPart;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.NotFoundException;
import ru.dipech.listeneng.repository.EntryRepository;

import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntrySpeechServiceImpl implements EntrySpeechService {

    private final EntryRepository entryRepository;
    private final TextSpeechService textSpeechService;

    @Override
    @Transactional
    public Path speech(User user, UUID entryId, EntryPart entryPart) {
        var optionalEntry = entryRepository.findBySectionUserAndId(user, entryId);
        if (optionalEntry.isEmpty()) {
            throw new NotFoundException("entry");
        }
        var entry = optionalEntry.get();
        var text = entryPart == EntryPart.PHRASE ? entry.getPhrase() : entry.getTranslation();
        return textSpeechService.speech(text);
    }

}
