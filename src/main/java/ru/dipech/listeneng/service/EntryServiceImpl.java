package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.persistence.Entry;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.NotFoundException;
import ru.dipech.listeneng.repository.EntryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;

    @Override
    public Entry getById(User user, UUID id) {
        var optionalEntry = entryRepository.findBySectionUserAndId(user, id);
        if (optionalEntry.isEmpty()) {
            throw new NotFoundException("Entry");
        }
        return optionalEntry.get();
    }

}
