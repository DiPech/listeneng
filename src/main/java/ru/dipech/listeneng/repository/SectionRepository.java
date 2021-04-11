package ru.dipech.listeneng.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dipech.listeneng.entity.persistence.Section;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SectionRepository extends CrudRepository<Section, UUID> {

    List<Section> findAllByUser(User user);

    List<Section> findAllByUserAndParentIsNull(User user);

    List<Section> findAllByUserAndIdIn(User user, List<UUID> ids);

    List<Section> findAllByUserAndIdInOrderByGlobalPriority(User user, List<UUID> ids);

    Optional<Section> findFirstByUserAndId(User user, UUID id);

    Optional<Section> findByUserAndName(User user, String name);

}
