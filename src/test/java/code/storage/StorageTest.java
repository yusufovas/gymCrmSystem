package code.storage;

import code.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }

    @Test
    void testStoreAndRetrieveEntitySuccessful() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1);
        trainee.setFirstName("Alice");

        storage.put("trainee", 1, trainee);

        Object result = storage.get("trainee", 1);

        assertNotNull(result);
        assertEquals("Alice", ((Trainee) result).getFirstName());
    }

    @Test
    void testGetWhenEntityNotFound() {
        Object result = storage.get("trainee", 99);
        assertNull(result);
    }

    @Test
    void testGetDataMap() {
        storage.put("trainee", 1, new Trainee());

        Map<String, Map<Integer, Object>> dataMap = storage.getDataMap();

        assertTrue(dataMap.containsKey("trainee"));
    }
}
