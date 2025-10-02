package code.dao;

import code.config.Config;
import code.model.Trainer;
import code.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Config.class)
@Transactional
class TrainerDaoTest {

    @Autowired
    private TrainerDao trainerDao;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .username("john.doe")
                        .password("secret")
                        .isActive(true)
                        .build())
                .specialization("Yoga")
                .build();

        trainerDao.create(trainer);
    }

    @Test
    void testCreateAndFindById() {
        Optional<Trainer> found = trainerDao.findById(trainer.getTrainerId());
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getUser().getFirstName());
    }

    @Test
    void testUpdate() {
        trainer.getUser().setLastName("Smith");
        Trainer updated = trainerDao.update(trainer);

        assertEquals("Smith", updated.getUser().getLastName());
    }

    @Test
    void testDeleteByUsername() {
        trainerDao.deleteByUsername("john.doe");
        assertTrue(trainerDao.findByUsername("john.doe").isEmpty());
    }
}
