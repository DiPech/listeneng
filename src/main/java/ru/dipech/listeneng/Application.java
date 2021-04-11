package ru.dipech.listeneng;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import ru.dipech.listeneng.service.property.EnvPropertySourceFactory;

@Slf4j
@ServletComponentScan
@SpringBootApplication
@PropertySources({
    @PropertySource(value = "file:${CONF_DIR}/app.env", factory = EnvPropertySourceFactory.class),
    @PropertySource("classpath:application.properties")
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
