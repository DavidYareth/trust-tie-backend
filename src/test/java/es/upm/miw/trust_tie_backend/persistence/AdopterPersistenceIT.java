package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.AdopterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdopterPersistenceIT {

    @InjectMocks
    private AdopterPersistence adopterPersistence;

    @Mock
    private AdopterRepository adopterRepository;

    private AdopterEntity adopterEntity;
    private Adopter adopter;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = UserEntity.builder()
                .userUuid(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .role(Role.ADOPTER)
                .build();

        User user = User.builder()
                .userUuid(userEntity.getUserUuid())
                .email("test@example.com")
                .password("password")
                .role(Role.ADOPTER)
                .build();

        adopterEntity = AdopterEntity.builder()
                .adopterUuid(UUID.randomUUID())
                .user(userEntity)
                .firstName("John")
                .lastName("Doe")
                .phone("123456789")
                .biography("Biography")
                .images("image1.jpg,image2.jpg")
                .build();

        adopter = Adopter.builder()
                .user(user)
                .firstName("John")
                .lastName("Doe")
                .phone("123456789")
                .biography("Biography")
                .images("image1.jpg,image2.jpg")
                .build();
    }

    @Test
    void testCreateAdopterSuccess() {
        when(adopterRepository.existsByFirstNameAndLastNameAndPhone(adopter.getFirstName(), adopter.getLastName(), adopter.getPhone())).thenReturn(false);
        when(adopterRepository.save(any(AdopterEntity.class))).thenReturn(adopterEntity);

        AdopterEntity createdAdopterEntity = adopterPersistence.create(adopter);
        assertNotNull(createdAdopterEntity);
        assertEquals(adopter.getFirstName(), createdAdopterEntity.getFirstName());

        verify(adopterRepository, times(1)).save(any(AdopterEntity.class));
    }

    @Test
    void testCreateAdopterAlreadyExists() {
        when(adopterRepository.existsByFirstNameAndLastNameAndPhone(adopter.getFirstName(), adopter.getLastName(), adopter.getPhone())).thenReturn(true);

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> adopterPersistence.create(adopter));
        assertTrue(thrown.getMessage().contains("Adopter already exists"));
    }

    @Test
    void testFindByAdopterUuidSuccess() {
        when(adopterRepository.findByAdopterUuid(adopterEntity.getAdopterUuid())).thenReturn(Optional.of(adopterEntity));

        AdopterEntity foundAdopterEntity = adopterPersistence.findByAdopterUuid(adopterEntity.getAdopterUuid());
        assertNotNull(foundAdopterEntity);
        assertEquals(adopterEntity.getFirstName(), foundAdopterEntity.getFirstName());
    }

    @Test
    void testFindByAdopterUuidNotFound() {
        when(adopterRepository.findByAdopterUuid(adopterEntity.getAdopterUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> adopterPersistence.findByAdopterUuid(adopterEntity.getAdopterUuid()));
        assertTrue(thrown.getMessage().contains("Adopter not found: " + adopterEntity.getAdopterUuid()));
    }

    @Test
    void testUpdateAdopterSuccess() {
        when(adopterRepository.findByAdopterUuid(adopterEntity.getAdopterUuid())).thenReturn(Optional.of(adopterEntity));
        when(adopterRepository.save(any(AdopterEntity.class))).thenReturn(adopterEntity);

        AdopterEntity updatedAdopterEntity = adopterPersistence.update(adopterEntity.getAdopterUuid(), adopter);
        assertNotNull(updatedAdopterEntity);
        assertEquals(adopter.getFirstName(), updatedAdopterEntity.getFirstName());

        verify(adopterRepository, times(1)).save(any(AdopterEntity.class));
    }

    @Test
    void testUpdateAdopterNotFound() {
        when(adopterRepository.findByAdopterUuid(adopterEntity.getAdopterUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> adopterPersistence.update(adopterEntity.getAdopterUuid(), adopter));
        assertTrue(thrown.getMessage().contains("Adopter not found: " + adopterEntity.getAdopterUuid()));
    }

    @Test
    void testDeleteAdopterSuccess() {
        when(adopterRepository.findByAdopterUuid(adopterEntity.getAdopterUuid())).thenReturn(Optional.of(adopterEntity));
        doNothing().when(adopterRepository).delete(adopterEntity);

        UUID userUuid = adopterPersistence.delete(adopterEntity.getAdopterUuid());
        assertNotNull(userUuid);
        assertEquals(userEntity.getUserUuid(), userUuid);

        verify(adopterRepository, times(1)).delete(adopterEntity);
    }

    @Test
    void testDeleteAdopterNotFound() {
        when(adopterRepository.findByAdopterUuid(adopterEntity.getAdopterUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> adopterPersistence.delete(adopterEntity.getAdopterUuid()));
        assertTrue(thrown.getMessage().contains("Adopter not found: " + adopterEntity.getAdopterUuid()));
    }
}
