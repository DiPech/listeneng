package ru.dipech.listeneng.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DevelopmentServiceImpl implements DevelopmentService {

    private final String appEnv;

    public DevelopmentServiceImpl(
        @Value("${app.env}") String appEnv
    ) {
        this.appEnv = appEnv;
    }

    @Override
    public boolean isDev() {
        return appEnv.equals("dev");
    }

    @Override
    public boolean isProd() {
        return !isDev();
    }

}
