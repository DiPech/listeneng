package ru.dipech.listeneng.service;

import ru.dipech.listeneng.entity.persistence.StatisticValue;
import ru.dipech.listeneng.entity.persistence.User;

import java.util.UUID;

public interface StatisticService {

    void add(User user, UUID entryId, StatisticValue value);

}
