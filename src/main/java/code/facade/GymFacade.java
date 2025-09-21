package code.facade;

import code.service.TraineeService;
import code.service.TrainerService;
import code.service.TrainingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class GymFacade {
    private static final Logger log = LogManager.getLogger(GymFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        log.info("GymFacade initialized with TraineeService, TrainerService, and TrainingService");
    }

    public TraineeService getTraineeService() {
        log.debug("Accessing TraineeService through facade");
        return traineeService;
    }

    public TrainerService getTrainerService() {
        log.debug("Accessing TrainerService through facade");
        return trainerService;
    }

    public TrainingService getTrainingService() {
        log.debug("Accessing TrainingService through facade");
        return trainingService;
    }
}
