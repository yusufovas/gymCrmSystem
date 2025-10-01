package code.dao;

import code.model.Trainee;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TraineeDao extends AbstractDao<Trainee, UUID> {
    public TraineeDao(SessionFactory sessionFactory) {
        super(sessionFactory, Trainee.class);
    }

    public Optional<Trainee> findByUsername(String username) {
        return getSession()
                .createQuery("from Trainee t where t.user.username = :username", Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public void deleteByUsername(String username) {
        findByUsername(username).ifPresent(e -> getSession().remove(e));
    }
}