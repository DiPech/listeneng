package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.persistence.Statistic;
import ru.dipech.listeneng.entity.persistence.StatisticValue;
import ru.dipech.listeneng.entity.persistence.User;
import ru.dipech.listeneng.exception.NotFoundException;
import ru.dipech.listeneng.repository.EntryRepository;
import ru.dipech.listeneng.repository.StatisticRepository;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final EntryRepository entryRepository;
    private final StatisticRepository statisticRepository;

    @Override
    @Transactional
    public void add(User user, UUID entryId, StatisticValue value) {
        var optionalEntry = entryRepository.findBySectionUserAndId(user, entryId);
        if (optionalEntry.isEmpty()) {
            throw new NotFoundException("Entry");
        }
        var statistic = new Statistic().setEntry(optionalEntry.get()).setValue(value).setCreatedAt(new Date());
        statisticRepository.save(statistic);
    }

}
