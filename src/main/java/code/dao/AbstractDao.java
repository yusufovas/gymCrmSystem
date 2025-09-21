package code.dao;

import code.storage.Storage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractDao<T> implements BaseDao<T> {
    private final Storage storage;
    private final String namespace;
    private final Function<T, Integer> idGetter;
    private final BiConsumer<T, Integer> idSetter;
    private final AtomicInteger counter = new AtomicInteger(0);

    protected AbstractDao(Storage storage, String namespace,
                          Function<T, Integer> idGetter,
                          BiConsumer<T, Integer> idSetter) {
        this.storage = storage;
        this.namespace = namespace;
        this.idGetter = idGetter;
        this.idSetter = idSetter;
    }

    @Override
    public T create(T entity) {
        Integer id = idGetter.apply(entity);
        if (id == null || id <= 0) {
            id = counter.incrementAndGet();
            idSetter.accept(entity, id);
        }
        storage.put(namespace, id, entity);
        return entity;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> findById(Integer id) {
        return Optional.ofNullable((T) storage.get(namespace, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return storage.getDataMap()
                .getOrDefault(namespace, Collections.emptyMap())
                .values()
                .stream()
                .map(o -> (T) o)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        Optional.ofNullable(storage.getDataMap().get(namespace))
                .ifPresent(map -> map.remove(id));
    }
}
