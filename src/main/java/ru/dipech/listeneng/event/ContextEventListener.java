package ru.dipech.listeneng.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.dipech.listeneng.repository.SectionRepository;
import ru.dipech.listeneng.service.DevelopmentService;
import ru.dipech.listeneng.service.InitialDataService;
import ru.dipech.listeneng.service.LoggerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContextEventListener {

    private final LoggerService logger;
    private final DevelopmentService developmentService;
    private final SectionRepository sectionRepository;
    private final InitialDataService initialDataService;

    @EventListener({ApplicationReadyEvent.class})
    public void onInit() {
        logger.init(log);
        if (developmentService.isProd()) {
            logger.log("Data shouldn't be populated in PROD env");
            return;
        }
        if (sectionRepository.count() > 1) {
            logger.log("Data shouldn't be populated if there is some another data");
            return;
        }
        logger.log("Populating data");
        initialDataService.populate(false);
        logger.log("Data has been populated");
    }

}
