package ru.dipech.listeneng.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dipech.listeneng.entity.persistence.Statistic;

import java.util.UUID;

public interface StatisticRepository extends CrudRepository<Statistic, UUID> {

}
