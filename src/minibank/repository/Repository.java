package minibank.repository;

import minibank.model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic repository using reflection on @Id annotation (Practical 13 Part B).
 * Reusable for any entity type, such as Account or Customer.
 */
public class Repository<T> {
    private final Map<String, T> storage = new HashMap<>();

    /**
     * Finds the field annotated with @Id in the entity class hierarchy and returns its string value.
     */
    private String extractId(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        Class<?> current = entity.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    try {
                        Object val = field.get(entity);
                        if (val != null) {
                            return val.toString();
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Could not read @Id field: " + field.getName(), e);
                    }
                }
            }
            current = current.getSuperclass();
        }
        throw new IllegalStateException("No field annotated with @Id found in " + entity.getClass().getName());
    }

    public synchronized T save(T entity) {
        String id = extractId(entity);
        storage.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final synchronized void saveAll(T... items) {
        if (items != null) {
            for (T item : items) {
                save(item);
            }
        }
    }

    public synchronized T findById(String id) {
        return storage.get(id);
    }

    public synchronized ArrayList<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public synchronized boolean delete(String id) {
        return storage.remove(id) != null;
    }

    public synchronized int count() {
        return storage.size();
    }

    /**
     * Generic bounded method: calculates average of any numeric array (Practical 13 Part B).
     */
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (N val : values) {
            if (val != null) {
                sum += val.doubleValue();
            }
        }
        return sum / values.length;
    }
}
