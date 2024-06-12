package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegisterOrganizationDtoTest {

    @Test
    void testToUser() {
        RegisterOrganizationDto registerOrganizationDto = RegisterOrganizationDto.builder()
                .email("org@example.com")
                .password("Password1!")
                .name("Organization")
                .phone("+987654321")
                .build();

        User user = registerOrganizationDto.toUser();

        assertNotNull(user);
        assertEquals("org@example.com", user.getEmail());
        assertEquals("Password1!", user.getPassword());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        RegisterOrganizationDto registerOrganizationDto = RegisterOrganizationDto.builder()
                .email("org@example.com")
                .password("Password1!")
                .name("Organization")
                .phone("+987654321")
                .build();

        assertNotNull(registerOrganizationDto);
        assertEquals("org@example.com", registerOrganizationDto.getEmail());
        assertEquals("Password1!", registerOrganizationDto.getPassword());
        assertEquals("Organization", registerOrganizationDto.getName());
        assertEquals("+987654321", registerOrganizationDto.getPhone());
    }
}
