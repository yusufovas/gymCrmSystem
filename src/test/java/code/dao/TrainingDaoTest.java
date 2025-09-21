package code.dao;

import code.model.Training;
import code.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoTest {

    private TrainingDao trainingDao;

    @BeforeEach
    void setUp() {
        Storage storage = new Storage();
        trainingDao = new TrainingDao(storage);
    }

    @Test
    void testCrete() {
        Training training = new Training();
        training.setTrainingName("Cardio");

        Training saved = trainingDao.create(training);

        assertTrue(saved.getTrainingId() > 0);
        assertEquals("Cardio", saved.getTrainingName());
    }

    @Test
    void testFindAll() {
        trainingDao.create(new Training());
        trainingDao.create(new Training());

        List<Training> trainings = trainingDao.findAll();

        assertEquals(2, trainings.size());
    }
    @Test
    void testGetAndSetTrainingDuration() {
        Training training = new Training();
        training.setTrainingName("Strength");
        training.setTrainingDuration("45");

        Training saved = trainingDao.create(training);

        assertEquals("45", saved.getTrainingDuration());
    }


}

