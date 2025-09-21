package code.utils;

import java.util.function.Predicate;

public class UsernameGenerator {
    public static String generate(String firstName, String lastName, Predicate<String> existsCheck) {
        String base = firstName + "." + lastName;
        int counter = 1;
        String candidate = base;
        while (existsCheck.test(candidate)) {
            candidate = base + counter++;
        }
        return candidate;
    }
}