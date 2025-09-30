package code.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T, ID> {
    T create(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(ID id);
}
