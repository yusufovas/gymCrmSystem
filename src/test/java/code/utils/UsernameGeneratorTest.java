package code.utils;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsernameGeneratorTest {

    @Test
    void testGenerateWhenNameIsUnique() {
        String username = UsernameGenerator.generate("John", "Doe", u -> false);

        assertEquals("John.Doe", username);
    }

    @Test
    void testGenerateWhenNameIsNotUnique() {
        Set<String> existing = new HashSet<>();
        existing.add("John.Doe");

        String username = UsernameGenerator.generate("John", "Doe", existing::contains);

        assertEquals("John.Doe1", username);
    }
}

