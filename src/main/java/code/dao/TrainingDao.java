package code.dao;

import code.model.Training;
import code.storage.Storage;
import org.springframework.stereotype.Repository;


@Repository
public class TrainingDao extends AbstractDao<Training> {
    private static final String NAMESPACE = "training";

    public TrainingDao(Storage storage) {
        super(storage, NAMESPACE, Training::getTrainingId, Training::setTrainingId);
    }
}
