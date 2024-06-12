package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Animal;
import es.upm.miw.trust_tie_backend.model.Organization;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AnimalDtoTest {

    @Test
    void testAnimalDtoConstructor() {
        UUID animalUuid = UUID.randomUUID();
        UUID organizationUuid = UUID.randomUUID();
        Organization organization = Organization.builder().organizationUuid(organizationUuid).build();
        Animal animal = Animal.builder()
                .animalUuid(animalUuid)
                .organization(organization)
                .name("Buddy")
                .type("Dog")
                .breed("Labrador")
                .age(5)
                .size("Medium")
                .characteristics("Friendly and active")
                .build();

        AnimalDto animalDto = new AnimalDto(animal);

        assertEquals(animalUuid.toString(), animalDto.getAnimalUuid());
        assertEquals(organizationUuid.toString(), animalDto.getOrganizationUuid());
        assertEquals("Buddy", animalDto.getName());
        assertEquals("Dog", animalDto.getType());
        assertEquals("Labrador", animalDto.getBreed());
        assertEquals(5, animalDto.getAge());
        assertEquals("Medium", animalDto.getSize());
        assertEquals("Friendly and active", animalDto.getCharacteristics());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        AnimalDto animalDto = AnimalDto.builder()
                .animalUuid(UUID.randomUUID().toString())
                .organizationUuid(UUID.randomUUID().toString())
                .name("Buddy")
                .type("Dog")
                .breed("Labrador")
                .age(5)
                .size("Medium")
                .characteristics("Friendly and active")
                .build();

        assertNotNull(animalDto);
        assertEquals("Buddy", animalDto.getName());
        assertEquals("Dog", animalDto.getType());
        assertEquals("Labrador", animalDto.getBreed());
        assertEquals(5, animalDto.getAge());
        assertEquals("Medium", animalDto.getSize());
        assertEquals("Friendly and active", animalDto.getCharacteristics());
    }
}
