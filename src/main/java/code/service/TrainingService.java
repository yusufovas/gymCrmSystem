package code.service;

import code.dao.TraineeDao;
import code.dao.TrainerDao;
import code.dao.TrainingDao;
import code.model.Training;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    private static final Logger log = LogManager.getLogger(TrainingService.class);

    private TrainingDao trainingDao;
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao,
                               TraineeDao traineeDao,
                               TrainerDao trainerDao) {
        this.trainingDao = trainingDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
    }

    public Training create(Training training) {
        validateTraining(training);

        log.info("New training created: type={} date={}",
                training.getTrainingType(), training.getTrainingDate());

        return trainingDao.create(training);
    }

    public List<Training> getAll() {
        log.info("Fetching all trainings");
        return trainingDao.findAll();
    }

    private void validateTraining(Training training) {
        if (training == null) {
            throw new IllegalArgumentException("Training cannot be null");
        }

        traineeDao.findById(training.getTraineeId())
                .orElseThrow(() -> new IllegalArgumentException("Trainee with ID " + training.getTraineeId() + " does not exist"));

        trainerDao.findById(training.getTrainerId())
                .orElseThrow(() -> new IllegalArgumentException("Trainer with ID " + training.getTrainerId() + " does not exist"));

        if (training.getTrainingType() == null || training.getTrainingType().isBlank()) {
            throw new IllegalArgumentException("Training type must not be empty");
        }
    }
}

