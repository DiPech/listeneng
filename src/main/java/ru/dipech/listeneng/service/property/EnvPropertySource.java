package ru.dipech.listeneng.service.property;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.env.PropertySource;

import java.nio.file.Path;

public class EnvPropertySource extends PropertySource<Path> {

    private Dotenv dotenv;

    public EnvPropertySource(String name, Path source) {
        super(name, source);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Object getProperty(String name) {
        ensureEnvFileLoaded();
        return dotenv.get(convertName(name));
    }

    /*
     * Examples:
     * app.url => APP_URL
     * app.database.name => APP_DATABASE_NAME
     */
    private String convertName(String name) {
        return name.toUpperCase().replaceAll("\\.", "_");
    }

    private void ensureEnvFileLoaded() {
        if (dotenv != null) {
            return;
        }
        dotenv = Dotenv.configure()
            .directory(source.getParent().toAbsolutePath().toString())
            .filename(source.getFileName().toString())
            .load();
    }

}
