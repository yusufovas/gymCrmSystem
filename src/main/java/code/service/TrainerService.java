package code.service;

import code.dao.BaseDao;
import code.dao.TrainerDao;
import code.dao.TrainingDao;
import code.model.Trainer;
import code.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainerService extends AbstractUserService<Trainer> {

    private final TrainerDao trainerDao;
    private final TrainingDao trainingDao;

    @Override
    public BaseDao<Trainer, UUID> getDao() {
        return trainerDao;
    }

    @Override
    protected void validate(Trainer trainer) {
        if (trainer.getUser().getFirstName() == null || trainer.getUser().getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (trainer.getUser().getLastName() == null || trainer.getUser().getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (trainer.getSpecialization() == null || trainer.getSpecialization().isBlank()) {
            throw new IllegalArgumentException("Specialization is required");
        }
    }

    @Override
    protected void cascadeDeleteIfNeeded(Trainer trainer) {
        trainingDao.findByTrainerAndCriteria(trainer.getUser().getUsername(), null, null, null)
                .forEach(training -> trainingDao.delete(training.getTrainingId()));
    }

    @Override
    protected User getUser(Trainer trainer) {
        return trainer.getUser();
    }
}
