package code.dao;

import code.model.Trainee;
import code.storage.Storage;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDao extends AbstractDao<Trainee> {
    private static final String NAMESPACE = "trainee";

    public TraineeDao(Storage storage) {
        super(storage, NAMESPACE, Trainee::getUserId, Trainee::setUserId);
    }
}