package ru.dipech.listeneng.util;

import lombok.NoArgsConstructor;
import ru.dipech.listeneng.entity.persistence.TreeEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

    public static <K, V> Map<K, V> listToMap(List<V> values, Function<V, K> keyMapper) {
        return values.stream()
            .collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public static <T> Set<T> findDifference(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.removeIf(set2::contains);
        return result;
    }

    public static <T> Set<T> findIntersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>();
        findUnion(set1, set2).forEach(item -> {
            if (set1.contains(item) && set2.contains(item)) {
                result.add(item);
            }
        });
        return result;
    }

    public static <T> Set<T> findUnion(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>();
        result.addAll(set1);
        result.addAll(set2);
        return result;
    }

    public static <K, V> Set<V> extractValuesByKeys(Map<K, V> map, Set<K> keys) {
        Set<V> result = new HashSet<>();
        keys.forEach(key -> {
            if (map.containsKey(key)) {
                result.add(map.get(key));
            }
        });
        return result;
    }

    public static <T extends TreeEntity<T, ID>, ID> List<T> flattenTree(List<T> entities) {
        List<T> result = new ArrayList<>();
        traverseTree(entities, result::add);
        return result;
    }

    public static <T extends TreeEntity<T, ID>, ID> List<ID> collectIds(List<T> entities) {
        List<ID> result = new ArrayList<>();
        traverseTree(entities, (entity) -> result.add(entity.getId()));
        return result;
    }

    public static <T extends TreeEntity<T, ID>, ID> void traverseTree(List<T> entities, Consumer<T> action) {
        entities.forEach(entity -> {
            action.accept(entity);
            traverseTree(entity.getChildren(), action);
        });
    }

    @NoArgsConstructor
    public static class MapBuilder<K, V> {

        private final Map<K, V> map = new HashMap<>();

        public MapBuilder<K, V> add(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return map;
        }

    }

}
