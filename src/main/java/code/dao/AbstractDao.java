package code.dao;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@AllArgsConstructor
public abstract class AbstractDao<T, ID> implements BaseDao<T, ID> {

    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }


    @Override
    public T create(T entity) {
        getSession().persist(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(getSession().get(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        return getSession()
                .createQuery("from " + entityClass.getSimpleName(),entityClass)
                .getResultList();
    }

    @Override
    public void delete(ID id) {
        findById(id).ifPresent(e -> getSession().remove(e));
    }

    @Override
    public T update(T entity) {
        return (T) getSession().merge(entity);
    }
}
