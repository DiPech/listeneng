package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dipech.listeneng.entity.persistence.*;
import ru.dipech.listeneng.exception.ApplicationException;
import ru.dipech.listeneng.repository.EntryRepository;
import ru.dipech.listeneng.repository.SectionRepository;
import ru.dipech.listeneng.util.DateUtils;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ru.dipech.listeneng.entity.persistence.User.ADMIN_UUID;

@Service
@RequiredArgsConstructor
public class InitialDataServiceImpl implements InitialDataService {

    private static final String ofPlaces = "Of places";
    private static final String myWifeIsWaitingForMeInTheTaxi = "My wife is waiting for me in the taxi.";

    private final UserService userService;
    private final SectionRepository sectionRepository;
    private final EntryRepository entryRepository;
    private final DataSource dataSource;

    private User user;
    private int globalPriorityCounter;

    @Override
    @Transactional
    public void populate(boolean force) {
        int sectionsCount = (int) sectionRepository.count();
        if (sectionsCount > 1 && !force) {
            throw new ApplicationException("Cannot populate, because some data already exists");
        }
        if (sectionsCount == 1 || force) {
            deleteOldData();
        }
        user = userService.getById(ADMIN_UUID);
        doPopulate();
    }

    private void deleteOldData() {
        sectionRepository.deleteAll(sectionRepository.findAll());
    }

    private void doPopulate() {
        globalPriorityCounter = 0;
        Section rootSection = makeSection("All sections", Arrays.asList(
            makeSection("Grammar", List.of(
                makeSection("Tenses", List.of(
                    makeSection("Present", List.of(
                        makeFinalSection("Simple", List.of(
                            makeTextEntry("We use this tense for:"),
                            makeTextEntry("1. Regular actions"),
                            makeTextEntry("2. True statements"),
                            makeTextEntry("3. Schedule / Fixed plans")
                        )),
                        makeFinalSection("Continuous"),
                        makeFinalSection("Perfect", List.of(
                            makeTextEntry("1. Something that started in the past and continues in the present."),
                            makePhraseEntry("They've been married for nearly fifty years.", "Они женаты почти 50 лет."),
                            makeTextEntry("2. When we are talking about our experience up to the present."),
                            makePhraseEntry("I've seen that film before.", "Я видел этот фильм ранее."),
                            makePhraseEntry("He has written three books and he is working on another one.", "Он написал три книги и он работает над другой."),
                            makeTextEntry("3. For something that happened in the past but is important in the present."),
                            makePhraseEntry("I can't get in the house, because I've lost my keys.", "Я не могу попасть домой, потому что я потерял ключи.")
                        )),
                        makeFinalSection("Perfect Continuous", List.of(
                            makeTextEntry("1. Actions that started in the past and continue in the present"),
                            makePhraseEntry("She has been waiting for you all day.", "Она ждала тебя целый день."),
                            makePhraseEntry("I've been working on this report since eight o'clock this morning.", "Я работаю над этим отчетом с восьми утра."),
                            makeTextEntry("2. Actions that have just finished, but we are interested in the results."),
                            makePhraseEntry("She has been cooking since last night.", "Она готовила еду с прошлой ночи."),
                            makePhraseEntry("It's been raining.", "Шёл дождь."),
                            makePhraseEntry("Someone's been eating my chips.", "Кто-то ел мои чипсы.")
                        ))
                    ))
                )),
                makeSection("Prepositions", List.of(
                    makeFinalSection(ofPlaces, List.of(
                        makeTextEntry("1. We use <b>at</b> for a point."),
                        makePhraseEntry("He's at the bus stop.", "Он на автобусной остановке.", List.of(
                            makeStatistic(StatisticValue.SUCCESS, makeDate(2020, 9, 10, 12, 5)),
                            makeStatistic(StatisticValue.SUCCESS, makeDate(2020, 9, 11, 15, 25)),
                            makeStatistic(StatisticValue.FAILURE, makeDate(2020, 9, 13, 18, 30)),
                            makeStatistic(StatisticValue.SKIPPED, makeDate(2020, 9, 15, 6, 45)),
                            makeStatistic(StatisticValue.SKIPPED, makeDate(2020, 9, 18, 21, 2)),
                            makeStatistic(StatisticValue.SUCCESS, makeDate(2020, 9, 20, 14, 58)),
                            makeStatistic(StatisticValue.FAILURE, makeDate(2020, 9, 21, 4, 3))
                        )),
                        makePhraseEntry("Someone knocked at the door.", "Кто-то постучал в дверь.", List.of(
                            makeStatistic(StatisticValue.SUCCESS, makeDate(2018, 3, 15, 12, 5)),
                            makeStatistic(StatisticValue.SUCCESS, makeDate(2019, 4, 5, 21, 25)),
                            makeStatistic(StatisticValue.FAILURE, makeDate(2020, 6, 7, 22, 8))
                        )),
                        makeTextEntry("2. We use <b>in</b> for an enclosed space."),
                        makePhraseEntry("They're in the garden.", "Они в саду."),
                        makePhraseEntry("John doesn't live in Paris.", "Джон не живет в Париже."),
                        makePhraseEntry("She left her keys in the car.", "Они оставила свои ключи в машине."),
                        makeTextEntry("3. We use <b>on</b> for a surface."),
                        makePhraseEntry("There is a spider on the wall.", "Здесь паук на стене."),
                        makeDividerEntry("Additional examples of using <b>at</b>"),
                        makeTextEntry("4. We also use <b>at</b> this way:"),
                        makePhraseEntry("The son is at home.", "Сын дома."),
                        makePhraseEntry("The daughter is at university.", "Дочь в университете."),
                        makeDividerEntry("Additional examples of using <b>in</b>"),
                        makeTextEntry("5. We also use <b>in</b> this way:"),
                        makePhraseEntry(myWifeIsWaitingForMeInTheTaxi, "Моя жена жжёт меня в такси."),
                        makePhraseEntry("The Sun is in the sky.", "Солнце в небе."),
                        makePhraseEntry("I've read it in a newspaper.", "Я прочитал это в газете.")
                    )),
                    makeFinalSection("Of directions")
                ))
            )),
            makeSection("Other", List.of(
                makeFinalSection("Common phrases", List.of(
                    makePhraseEntry("I didn't do it on purpose.", "Я сделал это не специально."),
                    makePhraseEntry("Let me get this straight.", "Позволь мне разобраться в этом."),
                    makePhraseEntry("It must be taken into account.", "Это необходимо принять во внимание."),
                    makePhraseEntry("Is that about right?", "Это похоже на правду?")
                )),
                makeFinalSection("Stable expressions", List.of(
                    makePhraseEntry("I'm fed up with it.", "Я сыт по горло этим."),
                    makePhraseEntry("This is out of my hands!", "Это не в моих силах!")
                ))
            ))
        ));
        sectionRepository.save(rootSection);
        // Special cases for flexible testing at client side
        replaceOneFullyFilledSectionIdToConst();
        replaceOnePhraseEntryIdToConst();
    }

    private void replaceOneFullyFilledSectionIdToConst() {
        var optionalSection = sectionRepository.findByUserAndName(user, ofPlaces);
        assert optionalSection.isPresent();
        var section = optionalSection.get();
        assert section.getId() != null;
        var oldId = section.getId().toString();
        var newId = "0b000b00-0000-0b0b-bb00-b00b00bbbbb0";
        var jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("UPDATE section SET id='" + newId + "' WHERE id='" + oldId + "'");
        jdbcTemplate.update("UPDATE entry SET section_id='" + newId + "' WHERE section_id='" + oldId + "'");
    }

    private void replaceOnePhraseEntryIdToConst() {
        var optionalEntry = entryRepository.findBySectionUserAndPhrase(user, myWifeIsWaitingForMeInTheTaxi);
        assert optionalEntry.isPresent();
        var entry = optionalEntry.get();
        assert entry.getId() != null;
        var oldId = entry.getId().toString();
        var newId = "0c000c00-0000-0c0c-cc00-c00c00ccccc0";
        var jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("UPDATE entry SET id='" + newId + "' WHERE id='" + oldId + "'");
    }

    private Section makeSection(String name, List<Section> children, List<Entry> entries) {
        int counter = 0;
        for (Section child : children) {
            child.setPriority(counter++);
        }
        var section = new Section().setUser(user);
        counter = 0;
        for (Entry entry : entries) {
            entry.setSection(section);
            entry.setPriority(counter++);
        }
        section
            .setName(name).setPriority(0).setGlobalPriority(globalPriorityCounter++)
            .setChildren(children).setEntries(entries);
        return section;
    }

    private Section makeSection(String name, List<Section> children) {
        return makeSection(name, children, List.of());
    }

    private Section makeFinalSection(String name, List<Entry> entries) {
        return makeSection(name, List.of(), entries);
    }

    private Section makeFinalSection(String name) {
        return makeSection(name, List.of(), List.of());
    }

    private Entry makeEntry(EntryType type, String text, String phrase, String translation, String divider,
                            List<Statistic> statistics) {
        var entry = new Entry();
        statistics.forEach(statistic -> statistic.setEntry(entry));
        return entry.setType(type).setDivider(divider).setText(text)
            .setPhrase(phrase).setTranslation(translation).setStatistics(statistics);
    }

    private Entry makeTextEntry(String text) {
        return makeEntry(EntryType.TEXT, text, null, null, null, List.of());
    }

    private Entry makeDividerEntry(String divider) {
        return makeEntry(EntryType.DIVIDER, null, null, null, divider, List.of());
    }

    private Entry makePhraseEntry(String phrase, String translation, List<Statistic> statistics) {
        return makeEntry(EntryType.PHRASE, null, phrase, translation, null, statistics);
    }

    private Entry makePhraseEntry(String phrase, String translation) {
        return makePhraseEntry(phrase, translation, List.of());
    }

    private Statistic makeStatistic(StatisticValue value, Date createdAt) {
        return new Statistic().setValue(value).setCreatedAt(createdAt);
    }

    private Date makeDate(int year, int month, int day, int hour, int minute) {
        return DateUtils.fromLocalDateTime(LocalDateTime.of(year, month, day, hour, minute));
    }

}
