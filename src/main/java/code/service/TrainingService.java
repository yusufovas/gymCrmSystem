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
        validateTraining(training);
        log.info("New training created: type={} date={} duration={}",
                training.getTrainingType().getTrainingTypeName(),
                training.getTrainingDate(),
                training.getTrainingDuration());
        return trainingDao.create(training);
    }

    @Transactional(readOnly = true)
    public List<Training> getAll() {
        log.info("Fetching all trainings");
        return trainingDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainingsForTrainee(String traineeUsername,
                                                 LocalDate from,
                                                 LocalDate to,
                                                 String trainerName,
                                                 String trainingType) {
        return trainingDao.findByTraineeAndCriteria(traineeUsername, from, to, trainerName, trainingType);
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainingsForTrainer(String trainerUsername,
                                                 LocalDate from,
                                                 LocalDate to,
                                                 String traineeName) {
        return trainingDao.findByTrainerAndCriteria(trainerUsername, from, to, traineeName);
    }

    private void validateTraining(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training cannot be null");
        }

        traineeDao.findById(training.getTrainee().getTraineeId())
                .orElseThrow(() -> new IllegalArgumentException("Trainee does not exist"));

        trainerDao.findById(training.getTrainer().getTrainerId())
                .orElseThrow(() -> new IllegalArgumentException("Trainer does not exist"));

        if (training.getTrainingType() == null) {
            throw new IllegalArgumentException("Training type must not be null");
        }

        if (training.getTrainingDuration() == null
                || training.getTrainingDuration() <= 0
                || training.getTrainingDuration() > 120) {
            throw new IllegalArgumentException("Training duration must be between 1 and 120 minutes");
        }

        if (training.getTrainingDate() == null || !training.getTrainingDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Training date must be in the future");
        }

        if (training.getTrainee().getDateOfBirth() == null || !training.getTrainee().getDateOfBirth().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Trainee date of birth must be in the past");
        }

        Period age = Period.between(training.getTrainee().getDateOfBirth(), training.getTrainingDate());
        if (age.getYears() < 16) {
            throw new IllegalArgumentException("Trainee must be at least 16 years old to attend the training");
        }
    }
}
