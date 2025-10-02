package code.service;

import code.dao.TraineeDao;
import code.dao.TrainingDao;
import code.model.Trainee;
import code.model.Training;
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

class TraineeServiceTest {

    private TraineeDao traineeDao;
    private TrainingDao trainingDao;
    private TraineeService service;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        traineeDao = mock(TraineeDao.class);
        trainingDao = mock(TrainingDao.class);

        service = new TraineeService(traineeDao, trainingDao);

        User user = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Alice")
                .lastName("Smith")
                .username("alice")
                .password("secret")
                .isActive(true)
                .build();

        trainee = Trainee.builder()
                .traineeId(UUID.randomUUID())
                .user(user)
                .dateOfBirth(LocalDate.of(2000, 5, 10))
                .address("Tashkent")
                .build();
    }

    @Test
    void testCreateTrainee_success() {
        when(traineeDao.create(any())).thenReturn(trainee);

        Trainee created = service.create(trainee, name -> false);

        assertNotNull(created);
        assertEquals("Alice", created.getUser().getFirstName());
        verify(traineeDao).create(trainee);
    }

    @Test
    void testCreateTrainee_invalidData() {
        trainee.getUser().setFirstName("");
        assertThrows(IllegalArgumentException.class,
                () -> service.create(trainee, name -> false));
    }

    @Test
    void testUpdateTrainee_success() {
        UUID id = trainee.getTraineeId();
        when(traineeDao.findById(id)).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any())).thenReturn(trainee);

        Trainee updated = Trainee.builder()
                .traineeId(id)
                .user(User.builder().firstName("Updated").lastName("Smith").build())
                .dateOfBirth(LocalDate.of(2000, 5, 10))
                .address("Samarkand")
                .build();

        Trainee result = service.update(id, updated, name -> false);

        assertEquals("Updated", result.getUser().getFirstName());
        verify(traineeDao).update(trainee);
    }

    @Test
    void testUpdateTrainee_notFound() {
        when(traineeDao.findById(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> service.update(UUID.randomUUID(), trainee, name -> false));
    }

    @Test
    void testDeleteByUsername_cascades() {
        when(traineeDao.findByUsername("alice")).thenReturn(Optional.of(trainee));
        when(trainingDao.findByTraineeAndCriteria(eq("alice"), any(), any(), any(), any()))
                .thenReturn(List.of(Training.builder()
                        .trainingId(UUID.randomUUID())
                        .trainee(trainee)
                        .trainingName("Morning Yoga")
                        .trainingDate(LocalDate.now().plusDays(2))
                        .trainingDuration(60)
                        .build()));
        doNothing().when(traineeDao).delete(any());

        service.deleteByUsername("alice");

        verify(trainingDao).delete(any());
        verify(traineeDao).delete(trainee.getUser().getUserId());
    }

    @Test
    void testAuthenticate_success() {
        when(traineeDao.findByUsername("alice")).thenReturn(Optional.of(trainee));
        assertTrue(service.authenticate("alice", "secret"));
    }

    @Test
    void testAuthenticate_wrongPassword() {
        when(traineeDao.findByUsername("alice")).thenReturn(Optional.of(trainee));
        assertFalse(service.authenticate("alice", "wrong"));
    }

    @Test
    void testChangePassword_success() {
        when(traineeDao.findByUsername("alice")).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any())).thenReturn(trainee);

        service.changePassword("alice", "secret", "newpass");

        assertEquals("newpass", trainee.getUser().getPassword());
        verify(traineeDao).update(trainee);
    }

    @Test
    void testToggleActive_success() {
        trainee.getUser().setActive(true);
        when(traineeDao.findByUsername("alice")).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any())).thenReturn(trainee);

        service.toggleActive("alice", false);

        assertFalse(trainee.getUser().isActive());
        verify(traineeDao).update(trainee);
    }

    @Test
    void testToggleActive_alreadyInState() {
        trainee.getUser().setActive(true);
        when(traineeDao.findByUsername("alice")).thenReturn(Optional.of(trainee));

        assertThrows(RuntimeException.class,
                () -> service.toggleActive("alice", true));
    }

    @Test
    void testGetAll() {
        when(traineeDao.findAll()).thenReturn(List.of(trainee));
        assertEquals(1, service.getAll().size());
    }
}
