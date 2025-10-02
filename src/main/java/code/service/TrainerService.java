package code.service;

import code.dao.BaseDao;
import code.dao.TrainerDao;
import code.dao.TrainingDao;
import code.dao.TrainingTypeDao;
import code.model.Trainer;
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
public class TrainerService extends AbstractUserService<Trainer> {

    private final TrainerDao trainerDao;
    private final TrainingDao trainingDao;
    private final TrainingTypeDao trainingTypeDao;
    private static final Logger log = LogManager.getLogger(TrainerService.class);

    @Override
    public BaseDao<Trainer, UUID> getDao() {
        return trainerDao;
    }

    @Override
    protected void validate(Trainer trainer) {
        log.debug("Validating trainer: {}", trainer);
        if (trainer.getUser().getFirstName() == null || trainer.getUser().getFirstName().isBlank()) {
            log.error("Validation failed: first name missing");
            throw new IllegalArgumentException("First name is required");
        }
        if (trainer.getUser().getLastName() == null || trainer.getUser().getLastName().isBlank()) {
            log.error("Validation failed: last name missing");
            throw new IllegalArgumentException("Last name is required");
        }
        if (trainer.getSpecialization() == null || trainer.getSpecialization().isBlank()) {
            log.error("Validation failed: specialization missing");
            throw new IllegalArgumentException("Specialization is required");
        }

        boolean specializationExists = trainingTypeDao.findAll().stream()
                .anyMatch(type -> type.getTrainingTypeName().equalsIgnoreCase(trainer.getSpecialization()));

        if (!specializationExists) {
            log.error("Validation failed: specialization '{}' not found in training types", trainer.getSpecialization());
            throw new IllegalArgumentException("Specialization " + trainer.getSpecialization() + " not found");
        }
    }

    @Override
    protected void cascadeDeleteIfNeeded(Trainer trainer) {
        log.info("Cascading delete for trainer={}", trainer.getUser().getUsername());
        trainingDao.findByTrainerAndCriteria(trainer.getUser().getUsername(), null, null, null)
                .forEach(training -> {
                    log.debug("Deleting training with ID={} for trainer={}", training.getTrainingId(), trainer.getUser().getUsername());
                    trainingDao.delete(training.getTrainingId());
                });
    }

    @Override
    protected User getUser(Trainer trainer) {
        return trainer.getUser();
    }
}
