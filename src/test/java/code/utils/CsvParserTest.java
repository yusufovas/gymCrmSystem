package code.utils;

import code.model.Trainee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    @Test
    void testGetTraineesSuccessful() {
        List<Trainee> trainees = CsvParser.parse("trainee-test-data.csv", Trainee.class);

        assertEquals(2, trainees.size());

        Trainee first = trainees.get(0);
        assertEquals("John", first.getFirstName());
        assertEquals("Doe", first.getLastName());
        assertEquals(1, first.getUserId());

        Trainee second = trainees.get(1);
        assertEquals("Jane", second.getFirstName());
        assertEquals("Smith", second.getLastName());
        assertEquals(2, second.getUserId());
    }

    @Test
    void testParseFileWhenFileNotFound() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                CsvParser.parse("nonExistent.csv", Trainee.class));

        assertTrue(ex.getMessage().contains("Failed to parse csv file"));
    }

    @Test
    void testParseWhenDataFileIsBroken() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                CsvParser.parse("broken-data.csv", Trainee.class));

        assertTrue(ex.getMessage().contains("Failed to parse csv file"));
    }
}

