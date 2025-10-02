package code.service;

import code.dao.BaseDao;
import code.dao.TraineeDao;
import code.dao.TrainingDao;
import code.model.Trainee;
import code.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TraineeService extends AbstractUserService<Trainee> {

    private final TraineeDao traineeDao;
    private final TrainingDao trainingDao;
    private static final Logger log = LogManager.getLogger(TraineeService.class);

    @Override
    public BaseDao<Trainee, UUID> getDao() {
        return traineeDao;
    }

    @Override
    protected void validate(Trainee trainee) {
        log.debug("Validating trainee: {}", trainee);
        if (trainee.getUser().getFirstName() == null || trainee.getUser().getFirstName().isBlank()) {
            log.error("Validation failed: first name missing");
            throw new IllegalArgumentException("First name is required");
        }
        if (trainee.getUser().getLastName() == null || trainee.getUser().getLastName().isBlank()) {
            log.error("Validation failed: last name missing");
            throw new IllegalArgumentException("Last name is required");
        }
        if (trainee.getDateOfBirth() == null) {
            log.error("Validation failed: date of birth missing");
            throw new IllegalArgumentException("Date of birth is required");
        }
    }

    @Override
    protected void cascadeDeleteIfNeeded(Trainee trainee) {
        log.info("Cascading delete for trainee={}", trainee.getUser().getUsername());
        trainingDao.findByTraineeAndCriteria(trainee.getUser().getUsername(), null, null, null, null)
                .forEach(training -> {
                    log.debug("Deleting training with ID={} for trainee={}", training.getTrainingId(), trainee.getUser().getUsername());
                    trainingDao.delete(training.getTrainingId());
                });
    }

    @Override
    protected User getUser(Trainee entity) {
        return entity.getUser();
    }
}
