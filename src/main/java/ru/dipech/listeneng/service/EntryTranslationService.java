package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.persistence.EntryPart;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.UUID;

public interface EntryTranslationService {

    String translate(User user, UUID entryId, EntryPart entryPart);

}
