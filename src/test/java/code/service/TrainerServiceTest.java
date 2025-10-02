package code.service;

import code.dao.TrainerDao;
import code.dao.TrainingDao;
import code.dao.TrainingTypeDao;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerServiceTest {

    private TrainerDao trainerDao;
    private TrainingDao trainingDao;
    private TrainingTypeDao trainingTypeDao;
    private TrainerService service;

    private Trainer trainer;
    private TrainingType cardioType;

    @BeforeEach
    void setUp() {
        trainerDao = mock(TrainerDao.class);
        trainingDao = mock(TrainingDao.class);
        trainingTypeDao = mock(TrainingTypeDao.class);

        service = new TrainerService(trainerDao, trainingDao, trainingTypeDao);

        User user = User.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .username("jdoe")
                .password("secret")
                .isActive(true)
                .build();

        cardioType = TrainingType.builder()
                .trainingTypeId(UUID.randomUUID())
                .trainingTypeName("Cardio")
                .build();

        trainer = Trainer.builder()
                .trainerId(UUID.randomUUID())
                .user(user)
                .specialization("Cardio")
                .build();
    }

    @Test
    void testCreateTrainer_success() {
        when(trainerDao.create(any())).thenReturn(trainer);
        when(trainingTypeDao.findAll()).thenReturn(List.of(cardioType));

        Trainer created = service.create(trainer, name -> false);

        assertNotNull(created);
        assertEquals("John", created.getUser().getFirstName());
        verify(trainerDao).create(trainer);
    }

    @Test
    void testCreateTrainer_invalidSpecialization() {
        when(trainingTypeDao.findAll()).thenReturn(List.of(
                TrainingType.builder().trainingTypeName("Yoga").build()));

        assertThrows(IllegalArgumentException.class,
                () -> service.create(trainer, name -> false));
    }

    @Test
    void testUpdateTrainer_success() {
        UUID id = trainer.getTrainerId();
        when(trainerDao.findById(id)).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer);
        when(trainingTypeDao.findAll()).thenReturn(List.of(cardioType));

        Trainer updated = Trainer.builder()
                .trainerId(id)
                .user(User.builder().firstName("Updated").lastName("Doe").build())
                .specialization("Cardio")
                .build();

        Trainer result = service.update(id, updated, name -> false);

        assertEquals("Updated", result.getUser().getFirstName());
        verify(trainerDao).update(trainer);
    }

    @Test
    void testUpdateTrainer_notFound() {
        when(trainerDao.findById(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> service.update(UUID.randomUUID(), trainer, name -> false));
    }

    @Test
    void testDeleteByUsername_cascades() {
        when(trainerDao.findByUsername("jdoe")).thenReturn(Optional.of(trainer));
        when(trainingDao.findByTrainerAndCriteria(eq("jdoe"), any(), any(), any()))
                .thenReturn(List.of(Training.builder()
                        .trainingId(UUID.randomUUID())
                        .trainer(trainer)
                        .trainingName("Morning Cardio")
                        .trainingDate(LocalDate.now().plusDays(5))
                        .trainingDuration(60)
                        .build()));
        doNothing().when(trainerDao).delete(any());

        service.deleteByUsername("jdoe");

        verify(trainingDao).delete(any());
        verify(trainerDao).delete(trainer.getUser().getUserId());
    }

    @Test
    void testAuthenticate_success() {
        when(trainerDao.findByUsername("jdoe")).thenReturn(Optional.of(trainer));
        assertTrue(service.authenticate("jdoe", "secret"));
    }

    @Test
    void testAuthenticate_wrongPassword() {
        when(trainerDao.findByUsername("jdoe")).thenReturn(Optional.of(trainer));
        assertFalse(service.authenticate("jdoe", "wrong"));
    }

    @Test
    void testChangePassword_success() {
        when(trainerDao.findByUsername("jdoe")).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer);

        service.changePassword("jdoe", "secret", "newpass");

        assertEquals("newpass", trainer.getUser().getPassword());
        verify(trainerDao).update(trainer);
    }

    @Test
    void testToggleActive_success() {
        trainer.getUser().setActive(true);
        when(trainerDao.findByUsername("jdoe")).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenReturn(trainer);

        service.toggleActive("jdoe", false);

        assertFalse(trainer.getUser().isActive());
        verify(trainerDao).update(trainer);
    }

    @Test
    void testToggleActive_alreadyInState() {
        trainer.getUser().setActive(true);
        when(trainerDao.findByUsername("jdoe")).thenReturn(Optional.of(trainer));

        assertThrows(RuntimeException.class,
                () -> service.toggleActive("jdoe", true));
    }

    @Test
    void testGetAll() {
        when(trainerDao.findAll()).thenReturn(List.of(trainer));
        assertEquals(1, service.getAll().size());
    }
}
