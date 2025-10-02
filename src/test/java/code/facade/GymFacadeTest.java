package code.facade;

import code.config.Config;
import code.dao.TraineeDao;
import code.dao.TrainerDao;
import code.dao.TrainingDao;
import code.dao.TrainingTypeDao;
import code.model.Trainee;
import code.model.Trainer;
import code.model.Training;
import code.model.TrainingType;
import code.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Config.class)
@Transactional
class GymFacadeTest {

    @Autowired
    private GymFacade facade;

    private Trainee trainee;

    private Trainer trainer;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Autowired
    private TrainingDao trainingDao;

    @BeforeEach
    void setUp() {
        Trainee trainee = traineeDao.create(Trainee.builder()
                .user(User.builder()
                        .firstName("Alice")
                        .lastName("Smith")
                        .username("alice")
                        .password("pass")
                        .isActive(true)
                        .build())
                .dateOfBirth(LocalDate.of(2000, 5, 10))
                .address("Tashkent")
                .build());

        Trainer trainer = trainerDao.create(Trainer.builder()
                .user(User.builder()
                        .firstName("Bob")
                        .lastName("Miller")
                        .username("bob")
                        .password("secret")
                        .isActive(true)
                        .build())
                .specialization("Cardio")
                .build());

        TrainingType type = trainingTypeDao.findAll().stream()
                .filter(t -> t.getTrainingTypeName().equalsIgnoreCase("Cardio"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("TrainingType 'Cardio' not found"));

        trainingDao.create(Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(type)
                .trainingName("Morning Cardio")
                .trainingDate(LocalDate.now().plusDays(3))
                .trainingDuration(60)
                .build());
    }

    @Test
    void testCreateAndAuthenticate() {
        assertTrue(facade.authenticateTrainee("alice", "pass"));
        assertTrue(facade.authenticateTrainer("bob", "secret"));
    }

    @Test
    void testGetByUsername() {
        assertNotNull(facade.getTraineeByUsername("alice"));
        assertNotNull(facade.getTrainerByUsername("bob"));
    }

    @Test
    void testChangePassword() {
        facade.changeTraineePassword("alice", "pass", "newpass");
        assertTrue(facade.authenticateTrainee("alice", "newpass"));

        facade.changeTrainerPassword("bob", "secret", "newsecret");
        assertTrue(facade.authenticateTrainer("bob", "newsecret"));
    }

    @Test
    void testToggleActive() {
        facade.toggleTrainee("alice", false);
        assertFalse(facade.getTraineeByUsername("alice").getUser().isActive());

        facade.toggleTrainer("bob", false);
        assertFalse(facade.getTrainerByUsername("bob").getUser().isActive());
    }

    @Test
    void testAddAndGetTrainings() {
        List<Training> traineeTrainings = facade.getTraineeTrainings(
                "alice", LocalDate.now(), LocalDate.now().plusDays(10), "Miller", "Cardio");
        assertFalse(traineeTrainings.isEmpty());

        List<Training> trainerTrainings = facade.getTrainerTrainings(
                "bob", LocalDate.now(), LocalDate.now().plusDays(10), "Smith");
        assertFalse(trainerTrainings.isEmpty());
    }

    @Test
    void testDeleteTrainee() {
        facade.deleteTrainee("alice");
        assertThrows(RuntimeException.class, () -> facade.getTraineeByUsername("hello"));
    }

    @Test
    void testGetUnassignedTrainersForTrainee() {
        Trainee trainee = facade.createTraineeProfile(
                Trainee.builder()
                        .user(User.builder()
                                .firstName("Alice")
                                .lastName("Smith")
                                .build())
                        .dateOfBirth(LocalDate.of(2000, 1, 1))
                        .address("Tashkent")
                        .build()
        );

        Trainer trainer1 = facade.createTrainerProfile(
                Trainer.builder()
                        .user(User.builder()
                                .firstName("Bob")
                                .lastName("Miller")
                                .build())
                        .specialization("Yoga")
                        .build()
        );

        Trainer trainer2 = facade.createTrainerProfile(
                Trainer.builder()
                        .user(User.builder()
                                .firstName("Emma")
                                .lastName("Brown")
                                .build())
                        .specialization("Cardio")
                        .build()
        );

        List<Trainer> unassignedBefore = facade.getUnassignedTrainersForTrainee(trainee.getUser().getUsername());
        assertTrue(unassignedBefore.contains(trainer1));
        assertTrue(unassignedBefore.contains(trainer2));

        facade.updateTraineeTrainersList(
                trainee.getUser().getUsername(),
                List.of(trainer1.getTrainerId())
        );

        List<Trainer> unassignedAfter = facade.getUnassignedTrainersForTrainee(trainee.getUser().getUsername());
        assertFalse(unassignedAfter.contains(trainer1));
        assertTrue(unassignedAfter.contains(trainer2));

        assertEquals(unassignedBefore.size() - 1, unassignedAfter.size());
    }

    @Test
    void testUpdateTraineeTrainersList() {
        Trainee trainee = facade.createTraineeProfile(
                Trainee.builder()
                        .user(User.builder()
                                .firstName("Alice")
                                .lastName("Johnson")
                                .build())
                        .dateOfBirth(LocalDate.of(2000, 1, 1))
                        .address("Tashkent")
                        .build()
        );

        Trainer trainer1 = facade.createTrainerProfile(
                Trainer.builder()
                        .user(User.builder()
                                .firstName("Bob")
                                .lastName("Miller")
                                .build())
                        .specialization("Yoga")
                        .build()
        );

        Trainer trainer2 = facade.createTrainerProfile(
                Trainer.builder()
                        .user(User.builder()
                                .firstName("Emma")
                                .lastName("Brown")
                                .build())
                        .specialization("Cardio")
                        .build()
        );

        facade.updateTraineeTrainersList(
                trainee.getUser().getUsername(),
                List.of(trainer1.getTrainerId(), trainer2.getTrainerId())
        );

        Trainee updatedTrainee = facade.getTraineeByUsername(trainee.getUser().getUsername());

        assertNotNull(updatedTrainee.getTrainers());
        assertEquals(2, updatedTrainee.getTrainers().size());

        List<String> assignedUsernames = updatedTrainee.getTrainers().stream()
                .map(t -> t.getUser().getUsername())
                .toList();

        assertTrue(assignedUsernames.contains(trainer1.getUser().getUsername()));
        assertTrue(assignedUsernames.contains(trainer2.getUser().getUsername()));
    }

}
