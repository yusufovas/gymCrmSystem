package code.facade;

import code.model.Trainee;
import code.model.Trainer;
import code.model.Training;
import code.service.TraineeService;
import code.service.TrainerService;
import code.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GymFacade {
    private static final Logger log = LogManager.getLogger(GymFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Transactional
    public Trainee createTraineeProfile(Trainee trainee) {
        return traineeService.create(trainee, this::usernameExists);
    }

    @Transactional
    public Trainer createTrainerProfile(Trainer trainer) {
        return trainerService.create(trainer, this::usernameExists);
    }

    @Transactional
    public boolean authenticateTrainee(String username, String password) {
        return traineeService.authenticate(username, password);
    }

    @Transactional
    public boolean authenticateTrainer(String username, String password) {
        return trainerService.authenticate(username, password);
    }

    @Transactional
    public Trainee getTraineeByUsername(String username) {
        return traineeService.getDao().findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found: " + username));
    }

    @Transactional
    public Trainer getTrainerByUsername(String username) {
        return trainerService.getDao().findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found: " + username));
    }

    @Transactional
    public void changeTraineePassword(String username, String oldPwd, String newPwd) {
        traineeService.changePassword(username, oldPwd, newPwd);
    }

    @Transactional
    public void changeTrainerPassword(String username, String oldPwd, String newPwd) {
        trainerService.changePassword(username, oldPwd, newPwd);
    }

    @Transactional
    public Trainee updateTrainee(UUID id, Trainee updated) {
        return traineeService.update(id, updated, this::usernameExists);
    }

    @Transactional
    public Trainer updateTrainer(UUID id, Trainer updated) {
        return trainerService.update(id, updated, this::usernameExists);
    }

    @Transactional
    public void toggleTrainee(String username, boolean activate) {
        traineeService.toggleActive(username, activate);
    }

    @Transactional
    public void toggleTrainer(String username, boolean activate) {
        trainerService.toggleActive(username, activate);
    }

    @Transactional
    public void deleteTrainee(String username) {
        traineeService.deleteByUsername(username);
    }

    @Transactional
    public Training addTraining(Training training) {
        return trainingService.create(training);
    }

    @Transactional
    public List<Training> getTraineeTrainings(
            String traineeUsername,
            LocalDate from, LocalDate to,
            String trainerName, String trainingType) {
        return trainingService.getTrainingDao()
                .findByTraineeAndCriteria(traineeUsername, from, to, trainerName, trainingType);
    }

    @Transactional
    public List<Training> getTrainerTrainings(
            String trainerUsername,
            LocalDate from, LocalDate to,
            String traineeName) {
        return trainingService.getTrainingDao()
                .findByTrainerAndCriteria(trainerUsername, from, to, traineeName);
    }

    @Transactional
    public List<Trainer> getUnassignedTrainersForTrainee(String traineeUsername) {
        Trainee trainee = traineeService.getDao().findByUsername(traineeUsername)
                .orElseThrow(() -> new RuntimeException("Trainee not found: " + traineeUsername));

        List<Trainer> allTrainers = trainerService.getAll();
        Set<Trainer> assigned = trainee.getTrainers();

        return allTrainers.stream()
                .filter(tr -> !assigned.contains(tr))
                .toList();
    }

    @Transactional
    public void updateTraineeTrainersList(String traineeUsername, List<UUID> trainerIds) {
        Trainee trainee = traineeService.getDao().findByUsername(traineeUsername)
                .orElseThrow(() -> new RuntimeException("Trainee not found: " + traineeUsername));

        Set<Trainer> trainers = trainerIds.stream()
                .map(id -> trainerService.getDao().findById(id)
                        .orElseThrow(() -> new RuntimeException("Trainer not found with id=" + id)))
                .collect(Collectors.toSet());

        trainee.setTrainers(trainers);
        traineeService.getDao().update(trainee);

        log.info("Updated trainee={} with trainers={}",
                trainee.getUser().getUsername(),
                trainers.stream().map(t -> t.getUser().getUsername()).toList());
    }

    @Transactional(readOnly = true)
    private boolean usernameExists(String username) {
        return traineeService.getAll().stream()
                .anyMatch(t -> t.getUser().getUsername().equals(username))
                || trainerService.getAll().stream()
                .anyMatch(t -> t.getUser().getUsername().equals(username));
    }

    @Transactional(readOnly = true)
    public TraineeService getTraineeService() {
        return traineeService;
    }
}
