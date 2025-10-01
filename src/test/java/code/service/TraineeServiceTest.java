//package code.service;
//
//import code.dao.TraineeDao;
//import code.model.Trainee;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TraineeServiceTest {
//
//    private TraineeDao traineeDao;
//    private TraineeService traineeService;
//
//    @BeforeEach
//    void setUp() {
//        traineeDao = Mockito.mock(TraineeDao.class);
//        traineeService = new TraineeService();
//        traineeService.setTraineeDao(traineeDao);
//    }
//
//    @Test
//    void testCreate() {
//        Trainee trainee = new Trainee();
//        trainee.setFirstName("John");
//        trainee.setLastName("Doe");
//
//        when(traineeDao.findAll()).thenReturn(List.of());
//
//        traineeService.create(trainee);
//
//        assertNotNull(trainee.getUsername());
//        assertNotNull(trainee.getPassword());
//        verify(traineeDao).create(any(Trainee.class));
//    }
//
//    @Test
//    void testUpdate() {
//        Trainee existing = new Trainee();
//        existing.setUserId(1);
//        existing.setFirstName("Old");
//        existing.setLastName("Name");
//
//        Trainee updated = new Trainee();
//        updated.setFirstName("New");
//        updated.setLastName("Name");
//        updated.setDateOfBirth(LocalDate.of(2000, 1, 1));
//
//        when(traineeDao.findById(1)).thenReturn(Optional.of(existing));
//        when(traineeDao.findAll()).thenReturn(List.of(existing));
//        when(traineeDao.create(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));
//
//
//        Trainee result = traineeService.update(1, updated);
//
//        assertEquals("New", result.getFirstName());
//        assertEquals("Name", result.getLastName());
//        assertEquals(LocalDate.of(2000, 1, 1), result.getDateOfBirth());
//    }
//
//    @Test
//    void testUnsuccessfulUpdate() {
//        when(traineeDao.findById(99)).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class,
//                () -> traineeService.update(99, new Trainee()));
//    }
//
//    @Test
//    void testDelete() {
//        traineeService.deleteProfile(1);
//        verify(traineeDao).delete(1);
//    }
//
//    @Test
//    void testGetAndSetAddress() {
//        Trainee trainee = new Trainee();
//        trainee.setFirstName("Alice");
//        trainee.setLastName("Smith");
//        trainee.setAddress("Some Street");
//
//        when(traineeDao.findAll()).thenReturn(List.of());
//        when(traineeDao.create(any(Trainee.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        Trainee saved = traineeService.create(trainee);
//
//        assertEquals("Some Street", saved.getAddress());
//    }
//
//    @Test
//    void testGetAllTrainees() {
//        Trainee trainee1 = new Trainee();
//        trainee1.setFirstName("Michael");
//        trainee1.setLastName("Cors");
//        Trainee trainee2 = new Trainee();
//        trainee2.setFirstName("Liz");
//        trainee2.setLastName("Anders");
//
//        when(traineeDao.findAll()).thenReturn(List.of(trainee1, trainee2));
//
//        List<Trainee> result = traineeService.getAll();
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(trainee1));
//        assertTrue(result.contains(trainee2));
//    }
//}
