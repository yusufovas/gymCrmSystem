package code.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest {

    @Test
    void testGenerateSuccessful() {
        String password = PasswordGenerator.generate();

        assertEquals(10, password.length());
    }

    @Test
    void testGenerateForAllowedChars() {
        String password = PasswordGenerator.generate();

        assertTrue(password.matches("[A-Za-z0-9]+"));
    }

    @Test
    void testGenerateGeneratesDifferentPasswords() {
        String p1 = PasswordGenerator.generate();
        String p2 = PasswordGenerator.generate();

        assertNotEquals(p1, p2);
    }
}

