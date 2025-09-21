package code.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    T create(T entity);
    Optional<T> findById(Integer id);
    List<T> findAll();
    void delete(Integer id);
}
