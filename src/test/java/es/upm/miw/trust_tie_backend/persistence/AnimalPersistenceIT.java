package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import es.upm.miw.trust_tie_backend.persistence.entities.AnimalEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnimalPersistenceIT {

    @InjectMocks
    private AnimalPersistence animalPersistence;

    @Mock
    private AnimalRepository animalRepository;

    private AnimalEntity animalEntity;
    private OrganizationEntity organizationEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        organizationEntity = OrganizationEntity.builder()
                .organizationUuid(UUID.randomUUID())
                .name("Test Organization")
                .phone("123456789")
                .description("Description")
                .website("https://example.com")
                .images("image1.jpg,image2.jpg")
                .build();

        animalEntity = AnimalEntity.builder()
                .animalUuid(UUID.randomUUID())
                .organization(organizationEntity)
                .name("Test Animal")
                .type("Dog")
                .breed("Breed")
                .age(5)
                .size("Medium")
                .characteristics("Friendly")
                .build();
    }

    @Test
    void testFindAll() {
        when(animalRepository.findAll()).thenReturn(Collections.singletonList(animalEntity));

        List<AnimalEntity> animals = animalPersistence.findAll();
        assertNotNull(animals);
        assertFalse(animals.isEmpty());
        assertEquals(animalEntity.getName(), animals.get(0).getName());
    }

    @Test
    void testFindByUuidSuccess() {
        when(animalRepository.findById(animalEntity.getAnimalUuid())).thenReturn(Optional.of(animalEntity));

        AnimalEntity foundAnimalEntity = animalPersistence.findByUuid(animalEntity.getAnimalUuid());
        assertNotNull(foundAnimalEntity);
        assertEquals(animalEntity.getName(), foundAnimalEntity.getName());
    }

    @Test
    void testFindByUuidNotFound() {
        when(animalRepository.findById(animalEntity.getAnimalUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, this::findByUuid);
        assertTrue(thrown.getMessage().contains("Animal not found: " + animalEntity.getAnimalUuid()));
    }

    private void findByUuid() {
        animalPersistence.findByUuid(animalEntity.getAnimalUuid());
    }

    @Test
    void testFindByOrganizationUuid() {
        when(animalRepository.findByOrganization_OrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Collections.singletonList(animalEntity));

        List<AnimalEntity> animals = animalPersistence.findByOrganizationUuid(organizationEntity.getOrganizationUuid());
        assertNotNull(animals);
        assertFalse(animals.isEmpty());
        assertEquals(animalEntity.getName(), animals.get(0).getName());
    }

    @Test
    void testCreateAnimalSuccess() {
        when(animalRepository.save(any(AnimalEntity.class))).thenReturn(animalEntity);

        AnimalEntity createdAnimalEntity = animalPersistence.create(animalEntity);
        assertNotNull(createdAnimalEntity);
        assertEquals(animalEntity.getName(), createdAnimalEntity.getName());

        verify(animalRepository, times(1)).save(any(AnimalEntity.class));
    }

    @Test
    void testUpdateAnimalSuccess() {
        when(animalRepository.existsById(animalEntity.getAnimalUuid())).thenReturn(true);
        when(animalRepository.save(any(AnimalEntity.class))).thenReturn(animalEntity);

        AnimalEntity updatedAnimalEntity = animalPersistence.update(animalEntity);
        assertNotNull(updatedAnimalEntity);
        assertEquals(animalEntity.getName(), updatedAnimalEntity.getName());

        verify(animalRepository, times(1)).save(any(AnimalEntity.class));
    }

    @Test
    void testUpdateAnimalNotFound() {
        when(animalRepository.existsById(animalEntity.getAnimalUuid())).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, this::updateAnimal);
        assertTrue(thrown.getMessage().contains("Animal not found: " + animalEntity.getAnimalUuid()));
    }

    private void updateAnimal() {
        animalPersistence.update(animalEntity);
    }

    @Test
    void testDeleteAnimalSuccess() {
        when(animalRepository.existsById(animalEntity.getAnimalUuid())).thenReturn(true);
        doNothing().when(animalRepository).deleteById(animalEntity.getAnimalUuid());

        animalPersistence.delete(animalEntity.getAnimalUuid());
        verify(animalRepository, times(1)).deleteById(animalEntity.getAnimalUuid());
    }

    @Test
    void testDeleteAnimalNotFound() {
        when(animalRepository.existsById(animalEntity.getAnimalUuid())).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, this::deleteAnimal);
        assertTrue(thrown.getMessage().contains("Animal not found: " + animalEntity.getAnimalUuid()));
    }

    private void deleteAnimal() {
        animalPersistence.delete(animalEntity.getAnimalUuid());
    }
}
