package code.dao;

import code.config.Config;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Config.class)
@Transactional
class TrainingDaoTest {

    @Autowired
    private TrainingDao trainingDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    private Training training;

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
                .orElseThrow(() -> new IllegalStateException("TrainingType 'Cardio' not found in DB"));

        training = trainingDao.create(Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(type)
                .trainingName("Morning Cardio")
                .trainingDate(LocalDate.now().plusDays(5))
                .trainingDuration(60)
                .build());
    }

    @Test
    void testCreateAndFindById() {
        Optional<Training> found = trainingDao.findById(training.getTrainingId());
        assertTrue(found.isPresent());
        assertEquals("Morning Cardio", found.get().getTrainingName());
    }

    @Test
    void testFindAll() {
        List<Training> trainings = trainingDao.findAll();
        assertFalse(trainings.isEmpty());
    }

    @Test
    void testUpdate() {
        training.setTrainingName("Evening Cardio");
        Training updated = trainingDao.update(training);
        assertEquals("Evening Cardio", updated.getTrainingName());
    }

    @Test
    void testDelete() {
        UUID id = training.getTrainingId();
        trainingDao.delete(id);
        assertTrue(trainingDao.findById(id).isEmpty());
    }

    @Test
    void testFindByCriteria() {
        List<Training> list = trainingDao.findByTraineeAndCriteria("alice",
                LocalDate.now(), LocalDate.now().plusDays(10), "Miller", "Cardio");
        assertEquals(1, list.size());
    }

    @Test
    void testFindByTraineeAndCriteria_withAllFilters() {
        List<Training> list = trainingDao.findByTraineeAndCriteria("alice",
                LocalDate.now(), LocalDate.now().plusDays(10), "Miller", "Cardio");
        assertEquals(1, list.size());
    }

    @Test
    void testFindByTraineeAndCriteria_withOnlyUsername() {
        List<Training> list = trainingDao.findByTraineeAndCriteria("alice",
                null, null, null, null);
        assertFalse(list.isEmpty());
    }

    @Test
    void testFindByTraineeAndCriteria_withWrongTrainerName() {
        List<Training> list = trainingDao.findByTraineeAndCriteria("alice",
                null, null, "NonExistent", null);
        assertTrue(list.isEmpty());
    }

    @Test
    void testFindByTrainerAndCriteria_withAllFilters() {
        List<Training> list = trainingDao.findByTrainerAndCriteria("bob",
                LocalDate.now(), LocalDate.now().plusDays(10), "Smith");
        assertEquals(1, list.size());
    }

    @Test
    void testFindByTrainerAndCriteria_withOnlyUsername() {
        List<Training> list = trainingDao.findByTrainerAndCriteria("bob",
                null, null, null);
        assertFalse(list.isEmpty());
    }

    @Test
    void testFindByTrainerAndCriteria_withWrongTraineeName() {
        List<Training> list = trainingDao.findByTrainerAndCriteria("bob",
                null, null, "WrongName");
        assertTrue(list.isEmpty());
    }

    @Test
    void testDeleteRemovesFromFindAll() {
        UUID id = training.getTrainingId();
        assertFalse(trainingDao.findAll().isEmpty());

        trainingDao.delete(id);

        assertTrue(trainingDao.findAll().isEmpty());
        assertTrue(trainingDao.findById(id).isEmpty());
    }

}
