package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.persistence.EntryPart;
import ru.dipech.listeneng.entity.persistence.User;

import java.nio.file.Path;
import java.util.UUID;

public interface EntrySpeechService {

    Path speech(User user, UUID entryId, EntryPart entryPart);

}
