package ru.dipech.listeneng.service.property;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.dipech.listeneng.util.ThrowingSupplier.throwingSupplier;

@Service
public class EnvPropertySourceFactory implements PropertySourceFactory {

    @NotNull
    @Override
    public PropertySource<?> createPropertySource(String name, @NotNull EncodedResource resource) {
        Path path = throwingSupplier(() -> Paths.get(resource.getResource().getFile().getAbsolutePath())).normalize();
        return new EnvPropertySource("Env File Property Source", path);
    }

}
