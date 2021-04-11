package ru.dipech.listeneng.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dipech.listeneng.entity.FileScope;
import ru.dipech.listeneng.entity.persistence.File;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends CrudRepository<File, UUID> {

    List<File> findAllByExpiresAtIsLessThan(Date date);

    List<File> findAllByFileScope(FileScope fileScope);

    Optional<File> findByFileNameAndFileScope(String fileName, FileScope fileScope);

}
