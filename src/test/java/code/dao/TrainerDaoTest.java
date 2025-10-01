//package code.dao;
//
//import code.model.Trainer;
//import code.storage.Storage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TrainerDaoTest {
//
//    private TrainerDao trainerDao;
//
//    @BeforeEach
//    void setUp() {
//        Storage storage = new Storage();
//        trainerDao = new TrainerDao(storage);
//    }
//
//    @Test
//    void testCreate() {
//        Trainer trainer = new Trainer();
//        trainer.setFirstName("Bob");
//        trainer.setLastName("Johnson");
//
//        Trainer saved = trainerDao.create(trainer);
//
//        assertTrue(saved.getUserId() > 0);
//    }
//
//    @Test
//    void testFindAll() {
//        trainerDao.create(new Trainer());
//        trainerDao.create(new Trainer());
//
//        List<Trainer> trainers = trainerDao.findAll();
//
//        assertEquals(2, trainers.size());
//    }
//}
//
