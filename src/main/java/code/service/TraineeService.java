package code.service;

import code.dao.BaseDao;
import code.dao.TraineeDao;
import code.dao.TrainingDao;
import code.model.Trainee;
import code.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TraineeService extends AbstractUserService<Trainee> {

    private final TraineeDao traineeDao;
    private final TrainingDao trainingDao;

    @Override
    public BaseDao<Trainee, UUID> getDao() {
        return traineeDao;
    }

    @Override
    protected void validate(Trainee trainee) {
        if (trainee.getUser().getFirstName() == null || trainee.getUser().getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (trainee.getUser().getLastName() == null || trainee.getUser().getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (trainee.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
    }

    @Override
    protected void cascadeDeleteIfNeeded(Trainee trainee) {
        trainingDao.findByTraineeAndCriteria(trainee.getUser().getUsername(), null, null, null, null)
                .forEach(training -> trainingDao.delete(training.getTrainingId()));
    }

    @Override
    protected User getUser(Trainee entity) {
        return entity.getUser();
    }
}
