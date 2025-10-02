package code.service;

import code.dao.TraineeDao;
import code.dao.TrainerDao;
import code.dao.TrainingDao;
import code.model.Training;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingService {
    private static final Logger log = LogManager.getLogger(TrainingService.class);

    @Getter
    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    @Transactional
    public Training create(Training training) {
        log.debug("Entering create() for training: {}", training);
        validateTraining(training);
        log.info("New training created: type={} date={} duration={}",
                training.getTrainingType().getTrainingTypeName(),
                training.getTrainingDate(),
                training.getTrainingDuration());
        return trainingDao.create(training);
    }

    @Transactional(readOnly = true)
    public List<Training> getAll() {
        log.debug("Fetching all trainings");
        return trainingDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainingsForTrainee(String traineeUsername,
                                                 LocalDate from,
                                                 LocalDate to,
                                                 String trainerName,
                                                 String trainingType) {
        log.info("Fetching trainings for trainee={} from={} to={} trainer={} type={}",
                traineeUsername, from, to, trainerName, trainingType);
        return trainingDao.findByTraineeAndCriteria(traineeUsername, from, to, trainerName, trainingType);
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainingsForTrainer(String trainerUsername,
                                                 LocalDate from,
                                                 LocalDate to,
                                                 String traineeName) {
        log.info("Fetching trainings for trainer={} from={} to={} trainee={}",
                trainerUsername, from, to, traineeName);
        return trainingDao.findByTrainerAndCriteria(trainerUsername, from, to, traineeName);
    }

    private void validateTraining(Training training) {
        log.debug("Validating training={}", training);

        if (training == null) {
            log.error("Validation failed: training is null");
            throw new IllegalArgumentException("Training cannot be null");
        }

        traineeDao.findById(training.getTrainee().getTraineeId())
                .orElseThrow(() -> {
                    log.error("Validation failed: trainee not found for ID={}", training.getTrainee().getTraineeId());
                    return new IllegalArgumentException("Trainee does not exist");
                });

        trainerDao.findById(training.getTrainer().getTrainerId())
                .orElseThrow(() -> {
                    log.error("Validation failed: trainer not found for ID={}", training.getTrainer().getTrainerId());
                    return new IllegalArgumentException("Trainer does not exist");
                });

        if (training.getTrainingType() == null) {
            log.error("Validation failed: training type missing");
            throw new IllegalArgumentException("Training type must not be null");
        }

        if (training.getTrainingDuration() == null
                || training.getTrainingDuration() <= 0
                || training.getTrainingDuration() > 120) {
            log.error("Validation failed: invalid training duration={}", training.getTrainingDuration());
            throw new IllegalArgumentException("Training duration must be between 1 and 120 minutes");
        }

        if (training.getTrainingDate() == null || !training.getTrainingDate().isAfter(LocalDate.now())) {
            log.error("Validation failed: invalid training date={}", training.getTrainingDate());
            throw new IllegalArgumentException("Training date must be in the future");
        }

        if (training.getTrainee().getDateOfBirth() == null || !training.getTrainee().getDateOfBirth().isBefore(LocalDate.now())) {
            log.error("Validation failed: trainee date of birth invalid={}", training.getTrainee().getDateOfBirth());
            throw new IllegalArgumentException("Trainee date of birth must be in the past");
        }

        Period age = Period.between(training.getTrainee().getDateOfBirth(), training.getTrainingDate());
        if (age.getYears() < 16) {
            log.error("Validation failed: trainee too young, age={}", age.getYears());
            throw new IllegalArgumentException("Trainee must be at least 16 years old to attend the training");
        }

        log.debug("Training validation successful");
    }
}
