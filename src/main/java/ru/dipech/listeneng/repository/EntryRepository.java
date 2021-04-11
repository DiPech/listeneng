package ru.dipech.listeneng.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dipech.listeneng.entity.persistence.Entry;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.Optional;
import java.util.UUID;

public interface EntryRepository extends CrudRepository<Entry, UUID> {

    Optional<Entry> findBySectionUserAndId(User user, UUID id);

    Optional<Entry> findBySectionUserAndPhrase(User user, String name);

}
