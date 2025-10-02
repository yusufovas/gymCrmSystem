package code.dao;

import code.config.Config;
import code.model.Trainee;
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
class TraineeDaoTest {

    @Autowired
    private TraineeDao traineeDao;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = Trainee.builder()
                .user(User.builder()
                        .firstName("Alice")
                        .lastName("Smith")
                        .username("alice.smith")
                        .password("pass123")
                        .isActive(true)
                        .build())
                .dateOfBirth(LocalDate.of(2000, 5, 10))
                .address("Tashkent")
                .build();

        traineeDao.create(trainee);
    }

    @Test
    void testCreateAndFindById() {
        Optional<Trainee> found = traineeDao.findById(trainee.getTraineeId());
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getUser().getFirstName());
    }

    @Test
    void testFindAll() {
        List<Trainee> all = traineeDao.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testUpdate() {
        trainee.getUser().setLastName("Johnson");
        Trainee updated = traineeDao.update(trainee);

        assertEquals("Johnson", updated.getUser().getLastName());
    }

    @Test
    void testDelete() {
        UUID id = trainee.getTraineeId();
        traineeDao.delete(id);

        assertTrue(traineeDao.findById(id).isEmpty());
    }

    @Test
    void testFindByUsername() {
        Optional<Trainee> found = traineeDao.findByUsername("alice.smith");
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getUser().getFirstName());
    }

    @Test
    void testDeleteByUsername() {
        traineeDao.deleteByUsername("alice.smith");
        assertTrue(traineeDao.findByUsername("alice.smith").isEmpty());
    }
}
