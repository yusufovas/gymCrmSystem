package code.facade;

import code.service.TraineeService;
import code.service.TrainerService;
import code.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private static final Logger log = LogManager.getLogger(GymFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

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
