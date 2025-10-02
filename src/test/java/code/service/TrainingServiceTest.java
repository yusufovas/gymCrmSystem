package code.service;

import code.dao.TraineeDao;
import code.dao.TrainerDao;
import code.dao.TrainingDao;
import code.model.Trainee;
import code.model.Trainer;
import code.model.Training;
import code.model.TrainingType;
import code.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingServiceTest {

    private TrainingDao trainingDao;
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;
    private TrainingService service;

    private Trainee trainee;
    private Trainer trainer;
    private Training training;

    @BeforeEach
    void setUp() {
        trainingDao = mock(TrainingDao.class);
        traineeDao = mock(TraineeDao.class);
        trainerDao = mock(TrainerDao.class);

        service = new TrainingService(trainingDao, traineeDao, trainerDao);

        User traineeUser = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Alice")
                .lastName("Smith")
                .username("alice")
                .password("pass")
                .isActive(true)
                .build();
        trainee = Trainee.builder()
                .traineeId(UUID.randomUUID())
                .user(traineeUser)
                .dateOfBirth(LocalDate.of(2000, 5, 10))
                .address("Tashkent")
                .build();

        User trainerUser = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Bob")
                .lastName("Miller")
                .username("bob")
                .password("secret")
                .isActive(true)
                .build();
        trainer = Trainer.builder()
                .trainerId(UUID.randomUUID())
                .user(trainerUser)
                .specialization("Cardio")
                .build();

        TrainingType type = TrainingType.builder()
                .trainingTypeId(UUID.randomUUID())
                .trainingTypeName("Cardio")
                .build();

        training = Training.builder()
                .trainingId(UUID.randomUUID())
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(type)
                .trainingName("Morning Cardio")
                .trainingDate(LocalDate.now().plusDays(5))
                .trainingDuration(60)
                .build();
    }

    @Test
    void testCreateTraining_success() {
        when(traineeDao.findById(trainee.getTraineeId())).thenReturn(Optional.of(trainee));
        when(trainerDao.findById(trainer.getTrainerId())).thenReturn(Optional.of(trainer));
        when(trainingDao.create(any())).thenReturn(training);

        Training created = service.create(training);

        assertNotNull(created);
        assertEquals("Morning Cardio", created.getTrainingName());
        verify(trainingDao).create(training);
    }

    @Test
    void testCreateTraining_invalidDuration() {
        training.setTrainingDuration(200);
        assertThrows(IllegalArgumentException.class, () -> service.create(training));
    }

    @Test
    void testCreateTraining_dateInPast() {
        training.setTrainingDate(LocalDate.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> service.create(training));
    }

    @Test
    void testCreateTraining_traineeTooYoung() {
        trainee.setDateOfBirth(LocalDate.now().minusYears(10));
        assertThrows(IllegalArgumentException.class, () -> service.create(training));
    }

    @Test
    void testGetAllTrainings() {
        when(trainingDao.findAll()).thenReturn(List.of(training));
        List<Training> result = service.getAll();
        assertEquals(1, result.size());
        verify(trainingDao).findAll();
    }

    @Test
    void testGetTrainingsForTrainee() {
        when(trainingDao.findByTraineeAndCriteria("alice",
                null, null, null, null)).thenReturn(List.of(training));

        List<Training> result = service.getTrainingsForTrainee("alice", null, null, null, null);
        assertEquals(1, result.size());
        verify(trainingDao).findByTraineeAndCriteria("alice", null, null, null, null);
    }

    @Test
    void testGetTrainingsForTrainer() {
        when(trainingDao.findByTrainerAndCriteria("bob",
                null, null, null)).thenReturn(List.of(training));

        List<Training> result = service.getTrainingsForTrainer("bob", null, null, null);
        assertEquals(1, result.size());
        verify(trainingDao).findByTrainerAndCriteria("bob", null, null, null);
    }
}