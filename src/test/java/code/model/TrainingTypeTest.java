//package code.model;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TrainingTypeTest {
//
//    @Test
//    void testGetAndSetMethods() {
//        TrainingType type = new TrainingType();
//
//        type.setTrainingTypeId(1);
//        type.setTrainingTypeName("Cardio");
//
//        assertEquals(1, type.getTrainingTypeId());
//        assertEquals("Cardio", type.getTrainingTypeName());
//    }
//
//    @Test
//    void testEqualsAndHashcode() {
//        TrainingType t1 = new TrainingType();
//        t1.setTrainingTypeId(1);
//        t1.setTrainingTypeName("Strength");
//
//        TrainingType t2 = new TrainingType();
//        t2.setTrainingTypeId(1);
//        t2.setTrainingTypeName("Strength");
//
//        assertEquals(t1, t2);
//        assertEquals(t1.hashCode(), t2.hashCode());
//    }
//
//    @Test
//    void testToString() {
//        TrainingType type = new TrainingType();
//        type.setTrainingTypeId(5);
//        type.setTrainingTypeName("Yoga");
//
//        String str = type.toString();
//        assertTrue(str.contains("5"));
//        assertTrue(str.contains("Yoga"));
//    }
//}
