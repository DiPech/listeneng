package ru.dipech.listeneng.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import ru.dipech.listeneng.entity.persistence.BaseEntity;

import java.util.*;

import static ru.dipech.listeneng.util.ReflectionUtils.*;

@Service
public class FetchServiceImpl implements FetchService {

    @Override
    public void fetch(Object object) {
        Set<Object> known = new HashSet<>();
        fetchRecursive(object, known);
    }

    @Override
    public void fetch(Object object, String... paths) {
        for (String path : paths) {
            fetch(object, path);
        }
    }

    @Override
    public void fetch(Object object, String path) {
        Object value = invokeGetter(object, path);
        Hibernate.initialize(value);
    }

    private void fetchRecursive(Object object, Set<Object> known) {
        if (object == null) {
            return;
        }
        if (IGNORED_TYPES.contains(object.getClass())) {
            return;
        }
        if (known.contains(object)) {
            return;
        }
        known.add(object);
        if (object instanceof Collection) {
            for (Object item : (Collection<?>) object) {
                fetchRecursive(item, known);
            }
        }
        if (!(object instanceof BaseEntity)) {
            return;
        }
        Hibernate.initialize(object);
        getGetters(object).forEach(getter -> {
            Object result = invokeMethod(object, getter);
            Hibernate.initialize(result);
            fetchRecursive(result, known);
        });
    }

    private static final Set<Class<?>> IGNORED_TYPES = new HashSet<>(Arrays.asList(
        String.class,
        Date.class,
        Calendar.class,
        Boolean.class,
        Character.class,
        Byte.class,
        Short.class,
        Integer.class,
        Long.class,
        Float.class,
        Double.class,
        Void.class,
        UUID.class,
        Logger.class
    ));

}
