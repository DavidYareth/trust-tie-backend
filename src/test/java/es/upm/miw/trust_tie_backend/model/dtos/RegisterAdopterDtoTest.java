package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegisterAdopterDtoTest {

    @Test
    void testToUser() {
        RegisterAdopterDto registerAdopterDto = RegisterAdopterDto.builder()
                .email("adopter@example.com")
                .password("Password1")
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();

        User user = registerAdopterDto.toUser();

        assertNotNull(user);
        assertEquals("adopter@example.com", user.getEmail());
        assertEquals("Password1", user.getPassword());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        RegisterAdopterDto registerAdopterDto = RegisterAdopterDto.builder()
                .email("adopter@example.com")
                .password("Password1")
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();

        assertNotNull(registerAdopterDto);
        assertEquals("adopter@example.com", registerAdopterDto.getEmail());
        assertEquals("Password1", registerAdopterDto.getPassword());
        assertEquals("John", registerAdopterDto.getFirstName());
        assertEquals("Doe", registerAdopterDto.getLastName());
        assertEquals("+123456789", registerAdopterDto.getPhone());
    }
}
