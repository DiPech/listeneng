package ru.dipech.listeneng.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dipech.listeneng.service.JsonService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@EnableWebMvc
@Configuration
@EnableScheduling
public class WebMvcConfig implements WebMvcConfigurer {

    private final JsonService jsonService;
    private final Path storagePath;

    public WebMvcConfig(
        JsonService jsonService,
        @Value("${app.storage}") String storagePath
    ) {
        this.jsonService = jsonService;
        this.storagePath = Paths.get(storagePath);
    }

    @Override
    public void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(jsonService.providerObjectMapper()));
        converters.add(new ResourceHttpMessageConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/storage/**")
            .addResourceLocations("file:" + storagePath + "/public/");
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/public/static/");
    }

}
