package code.dao;

import code.config.Config;
import code.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Config.class)
@Transactional
class TrainingTypeDaoTest {

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Test
    void testFindAll() {
        List<TrainingType> types = trainingTypeDao.findAll();
        assertFalse(types.isEmpty());
    }

    @Test
    void testCreateThrowsException() {
        assertThrows(UnsupportedOperationException.class, () ->
                trainingTypeDao.create(TrainingType.builder().trainingTypeName("Pilates").build()));
    }

    @Test
    void testUpdateThrowsException() {
        assertThrows(UnsupportedOperationException.class, () ->
                trainingTypeDao.update(TrainingType.builder().trainingTypeName("Yoga").build()));
    }

    @Test
    void testDeleteThrowsException() {
        assertThrows(UnsupportedOperationException.class, () ->
                trainingTypeDao.delete(UUID.randomUUID()));
    }
}

