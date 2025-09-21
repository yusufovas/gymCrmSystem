package code.service;

import code.dao.TrainerDao;
import code.model.Trainee;
import code.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerDao trainerDao;
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerDao = Mockito.mock(TrainerDao.class);
        trainerService = new TrainerService();
        trainerService.setTrainerDao(trainerDao);
    }

    @Test
    void testCreate() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");

        when(trainerDao.findAll()).thenReturn(List.of());

        trainerService.create(trainer);

        assertNotNull(trainer.getUsername());
        assertNotNull(trainer.getPassword());
        verify(trainerDao).create(any(Trainer.class));
    }

    @Test
    void testUpdate() {
        Trainer existing = new Trainer();
        existing.setUserId(1);
        existing.setFirstName("Old");
        existing.setLastName("Trainer");

        Trainer updated = new Trainer();
        updated.setFirstName("New");
        updated.setLastName("Trainer");
        updated.setSpecialization("Fitness");

        when(trainerDao.findById(1)).thenReturn(Optional.of(existing));
        when(trainerDao.findAll()).thenReturn(List.of(existing));
        when(trainerDao.create(any(Trainer.class))).thenAnswer(inv -> inv.getArgument(0));

        Trainer result = trainerService.update(1, updated);

        assertEquals("New", result.getFirstName());
        assertEquals("Fitness", result.getSpecialization());
    }

    @Test
    void testUnsuccessfulUpdate() {
        when(trainerDao.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> trainerService.update(99, new Trainer()));
    }

    @Test
    void testGetAllTrainers() {
        Trainer trainer1 = new Trainer();
        trainer1.setFirstName("Joshua");
        trainer1.setLastName("Helen");

        Trainer trainer2 = new Trainer();
        trainer2.setFirstName("Margo");
        trainer2.setLastName("Williams");

        when(trainerDao.findAll()).thenReturn(List.of(trainer1, trainer2));

        List<Trainer> result = trainerService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(trainer1));
        assertTrue(result.contains(trainer2));
    }
}
