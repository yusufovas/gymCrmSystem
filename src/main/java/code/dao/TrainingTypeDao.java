package code.dao;

import code.model.TrainingType;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TrainingTypeDao extends AbstractDao<TrainingType, UUID> {
    public TrainingTypeDao(SessionFactory sessionFactory) {
        super(sessionFactory, TrainingType.class);
    }

    @Override
    public TrainingType create(TrainingType entity) {
        throw new UnsupportedOperationException("TrainingType is read-only");
    }

    @Override
    public TrainingType update(TrainingType entity) {
        throw new UnsupportedOperationException("TrainingType is read-only");
    }

    @Override
    public Optional<TrainingType> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void delete(UUID id) {
        throw new UnsupportedOperationException("TrainingType is read-only");
    }
}
