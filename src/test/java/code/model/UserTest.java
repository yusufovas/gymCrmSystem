package code.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();

        User user = User.builder()
                .userId(id)
                .firstName("Alice")
                .lastName("Smith")
                .username("alice")
                .password("pass123")
                .isActive(true)
                .build();

        assertEquals(id, user.getUserId());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("alice", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertTrue(user.isActive());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        UUID id = UUID.randomUUID();

        user.setUserId(id);
        user.setFirstName("Bob");
        user.setLastName("Miller");
        user.setUsername("bob");
        user.setPassword("secret");
        user.setActive(false);

        assertEquals(id, user.getUserId());
        assertEquals("Bob", user.getFirstName());
        assertEquals("Miller", user.getLastName());
        assertEquals("bob", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertFalse(user.isActive());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();

        User u1 = User.builder().userId(id).firstName("A").lastName("B").username("u").password("p").isActive(true).build();
        User u2 = User.builder().userId(id).firstName("A").lastName("B").username("u").password("p").isActive(true).build();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testNotEquals() {
        User u1 = User.builder().userId(UUID.randomUUID()).username("user1").build();
        User u2 = User.builder().userId(UUID.randomUUID()).username("user2").build();

        assertNotEquals(u1, u2);
    }

    @Test
    void testToStringContainsFields() {
        User user = User.builder()
                .userId(UUID.randomUUID())
                .firstName("Alice")
                .lastName("Smith")
                .username("alice")
                .password("pass123")
                .isActive(true)
                .build();

        String str = user.toString();
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("Smith"));
        assertTrue(str.contains("alice"));
    }

    @Test
    void testBuilderSetsAllFields() {
        UUID id = UUID.randomUUID();

        User user = User.builder()
                .userId(id)
                .firstName("Alice")
                .lastName("Smith")
                .username("alice")
                .password("secret")
                .isActive(true)
                .build();

        assertEquals(id, user.getUserId());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("alice", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertTrue(user.isActive());
    }

    @Test
    void testBuilderCreatesDistinctObjects() {
        User u1 = User.builder().username("u1").password("p1").isActive(true).build();
        User u2 = User.builder().username("u2").password("p2").isActive(false).build();

        assertNotEquals(u1, u2);
        assertEquals("u1", u1.getUsername());
        assertEquals("u2", u2.getUsername());
    }

    @Test
    void testBuilderChaining() {
        User user = User.builder()
                .firstName("Bob")
                .lastName("Miller")
                .username("bob")
                .build();

        assertEquals("Bob", user.getFirstName());
        assertEquals("Miller", user.getLastName());
        assertEquals("bob", user.getUsername());
        assertNull(user.getPassword());
        assertFalse(user.isActive());
    }
}
