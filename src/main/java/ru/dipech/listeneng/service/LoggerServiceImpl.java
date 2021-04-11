package ru.dipech.listeneng.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.exception.NotInitializedException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class LoggerServiceImpl implements LoggerService {

    private final DevelopmentService developmentService;

    private Logger logger;
    private String scope;

    @Override
    public void init(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void init(Logger logger, String scope) {
        this.logger = logger;
        this.scope = scope;
    }

    @Override
    public void log(String template, Object... objects) {
        ensureInit();
        if (developmentService.isDev()) {
            logger.info(getScopedTemplate(template), objects);
        } else {
            logger.debug(getScopedTemplate(template), objects);
        }
    }

    private String getScopedTemplate(String template) {
        return (scope != null ? "[" + scope + "] " : "") + template;
    }

    private void ensureInit() {
        if (logger == null) {
            throw new NotInitializedException();
        }
    }

}
