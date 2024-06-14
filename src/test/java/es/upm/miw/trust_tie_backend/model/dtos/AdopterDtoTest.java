package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AdopterDtoTest {

    @Test
    void testAdopterDtoConstructor() {
        User user = new User("test@example.com", "Password1", Role.ADOPTER);
        Adopter adopter = Adopter.builder()
                .adopterUuid(UUID.randomUUID())
                .user(user)
                .firstName("John")
                .lastName("Doe")
                .phone("123456789")
                .biography("A brief bio")
                .images("image1.jpg")
                .build();

        AdopterDto adopterDto = new AdopterDto(adopter);

        assertEquals(adopter.getAdopterUuid().toString(), adopterDto.getAdopterUuid());
        assertEquals("test@example.com", adopterDto.getEmail());
        assertEquals("John", adopterDto.getFirstName());
        assertEquals("Doe", adopterDto.getLastName());
        assertEquals("123456789", adopterDto.getPhone());
        assertEquals("A brief bio", adopterDto.getBiography());
        assertEquals("image1.jpg", adopterDto.getImages());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        AdopterDto adopterDto = AdopterDto.builder()
                .adopterUuid(UUID.randomUUID().toString())
                .email("builder@example.com")
                .password("Password1")
                .firstName("Builder")
                .lastName("User")
                .phone("987654321")
                .biography("Builder's bio")
                .images("builder_image.jpg")
                .build();

        assertNotNull(adopterDto);
        assertEquals("builder@example.com", adopterDto.getEmail());
        assertEquals("Password1", adopterDto.getPassword());
        assertEquals("Builder", adopterDto.getFirstName());
        assertEquals("User", adopterDto.getLastName());
        assertEquals("987654321", adopterDto.getPhone());
        assertEquals("Builder's bio", adopterDto.getBiography());
        assertEquals("builder_image.jpg", adopterDto.getImages());
    }
}
