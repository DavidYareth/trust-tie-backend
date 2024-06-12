package es.upm.miw.trust_tie_backend.model.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoginDtoTest {

    @Test
    void testAllArgsConstructor() {
        LoginDto loginDto = new LoginDto("test@example.com", "Password1");

        assertNotNull(loginDto);
        assertEquals("test@example.com", loginDto.getEmail());
        assertEquals("Password1", loginDto.getPassword());
    }

    @Test
    void testBuilder() {
        LoginDto loginDto = LoginDto.builder()
                .email("builder@example.com")
                .password("Password1")
                .build();

        assertNotNull(loginDto);
        assertEquals("builder@example.com", loginDto.getEmail());
        assertEquals("Password1", loginDto.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        LoginDto loginDto = LoginDto.builder()
                .email("noargs@example.com")
                .password("Password1")
                .build();

        assertNotNull(loginDto);
        assertEquals("noargs@example.com", loginDto.getEmail());
        assertEquals("Password1", loginDto.getPassword());
    }
}
