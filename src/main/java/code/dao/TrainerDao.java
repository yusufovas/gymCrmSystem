package code.dao;

import code.model.Trainer;
import code.storage.Storage;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDao extends AbstractDao<Trainer> {
    private static final String NAMESPACE = "trainer";

    public TrainerDao(Storage storage) {
        super(storage, NAMESPACE, Trainer::getUserId, Trainer::setUserId);
    }
}
