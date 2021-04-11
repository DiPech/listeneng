package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.persistence.Entry;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.UUID;

public interface EntryService {

    Entry getById(User user, UUID id);

}
