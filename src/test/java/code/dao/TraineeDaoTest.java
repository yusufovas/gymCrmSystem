//package code.dao;
//
//import code.model.Trainee;
//import code.storage.Storage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//class TraineeDaoTest {
//
//    private TraineeDao traineeDao;
//
//    @BeforeEach
//    void setUp() {
//        Storage storage = new Storage();
//        traineeDao = new TraineeDao(storage);
//    }
//
//    @Test
//    void testCreate() {
//        Trainee trainee = new Trainee();
//        trainee.setFirstName("Alice");
//        trainee.setLastName("Smith");
//        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
//
//        Trainee saved = traineeDao.create(trainee);
//
//        assertTrue(saved.getUserId() > 0);
//        assertEquals(saved, traineeDao.findById(saved.getUserId()).orElseThrow());
//    }
//
//    @Test
//    void testFindByIdWhenIdNotFound() {
//        Optional<Trainee> result = traineeDao.findById(999);
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void testFindAll() {
//        Trainee t1 = traineeDao.create(new Trainee());
//        Trainee t2 = traineeDao.create(new Trainee());
//
//        List<Trainee> trainees = traineeDao.findAll();
//
//        assertEquals(2, trainees.size());
//        assertTrue(trainees.contains(t1));
//        assertTrue(trainees.contains(t2));
//    }
//
//    @Test
//    void testDelete() {
//        Trainee trainee = traineeDao.create(new Trainee());
//
//        traineeDao.delete(trainee.getUserId());
//
//        assertTrue(traineeDao.findById(trainee.getUserId()).isEmpty());
//    }
//}
//
