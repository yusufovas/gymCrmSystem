//package code.service;
//
//import code.dao.TraineeDao;
//import code.dao.TrainerDao;
//import code.dao.TrainingDao;
//import code.model.Trainee;
//import code.model.Trainer;
//import code.model.Training;
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
//class TrainingServiceTest {
//
//    private TrainingDao trainingDao;
//    private TraineeDao traineeDao;
//    private TrainerDao trainerDao;
//    private TrainingService trainingService;
//
//    @BeforeEach
//    void setUp() {
//        trainingDao = Mockito.mock(TrainingDao.class);
//        traineeDao = Mockito.mock(TraineeDao.class);
//        trainerDao = Mockito.mock(TrainerDao.class);
//
//        trainingService = new TrainingService();
//        trainingService.setTrainingDao(trainingDao, traineeDao, trainerDao);
//    }
//
//    @Test
//    void testCreate() {
//        Training training = new Training();
//        training.setTrainingId(1);
//        training.setTraineeId(10);
//        training.setTrainerId(20);
//        training.setTrainingType("Cardio");
//        training.setTrainingDate(LocalDate.now());
//
//        when(traineeDao.findById(10)).thenReturn(Optional.of(new Trainee()));
//        when(trainerDao.findById(20)).thenReturn(Optional.of(new Trainer()));
//        when(trainingDao.create(training)).thenReturn(training);
//
//        Training result = trainingService.create(training);
//
//        assertNotNull(result);
//        verify(trainingDao).create(training);
//    }
//
//    @Test
//    void testUnsuccessfulCreateWhenTraineeNotFound() {
//        Training training = new Training();
//        training.setTraineeId(999);
//        training.setTrainerId(20);
//        training.setTrainingType("Yoga");
//
//        when(traineeDao.findById(999)).thenReturn(Optional.empty());
//        when(trainerDao.findById(20)).thenReturn(Optional.of(new Trainer()));
//
//        assertThrows(IllegalArgumentException.class,
//                () -> trainingService.create(training));
//    }
//
//    @Test
//    void testUnsuccessfulCreateWhenTrainerNotFound() {
//        Training training = new Training();
//        training.setTraineeId(10);
//        training.setTrainerId(999);
//        training.setTrainingType("Yoga");
//
//        when(traineeDao.findById(10)).thenReturn(Optional.of(new Trainee()));
//        when(trainerDao.findById(999)).thenReturn(Optional.empty());
//
//        assertThrows(IllegalArgumentException.class,
//                () -> trainingService.create(training));
//    }
//
//    @Test
//    void testUnsuccessfulCreateWhenTrainingTypeIsEmpty() {
//        Training training = new Training();
//        training.setTraineeId(10);
//        training.setTrainerId(20);
//        training.setTrainingType("");
//
//        when(traineeDao.findById(10)).thenReturn(Optional.of(new Trainee()));
//        when(trainerDao.findById(20)).thenReturn(Optional.of(new Trainer()));
//
//        assertThrows(IllegalArgumentException.class,
//                () -> trainingService.create(training));
//    }
//
//    @Test
//    void testGetAndSetTrainingDuration() {
//        Training training = new Training();
//        training.setTraineeId(10);
//        training.setTrainerId(20);
//        training.setTrainingType("Yoga");
//        training.setTrainingDuration("60");
//
//        when(traineeDao.findById(10)).thenReturn(Optional.of(new Trainee()));
//        when(trainerDao.findById(20)).thenReturn(Optional.of(new Trainer()));
//        when(trainingDao.create(any(Training.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        Training result = trainingService.create(training);
//
//        assertEquals("60", result.getTrainingDuration());
//    }
//
//    @Test
//    void testGetAllTrainings() {
//        Training training1 = new Training();
//        training1.setTrainingName("Yoga");
//        Training training2 = new Training();
//        training2.setTrainingName("Cardio");
//
//        when(trainingDao.findAll()).thenReturn(List.of(training1, training2));
//
//        List<Training> result = trainingService.getAll();
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains(training1));
//        assertTrue(result.contains(training2));
//    }
//
//
//
//}
