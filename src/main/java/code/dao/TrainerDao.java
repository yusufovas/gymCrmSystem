package code.dao;

import code.model.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainerDao extends AbstractDao<Trainer, UUID> {
    public TrainerDao(SessionFactory sessionFactory) {
        super(sessionFactory, Trainer.class);
    }

    public Optional<Trainer> findByUsername(String username) {
        return getSession()
                .createQuery("from Trainer t where t.user.username = :username", Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public void deleteByUsername(String username) {
        findByUsername(username).ifPresent(e -> getSession().remove(e));
    }
}
