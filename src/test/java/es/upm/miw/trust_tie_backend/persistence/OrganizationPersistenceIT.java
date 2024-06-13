package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationPersistenceIT {

    @InjectMocks
    private OrganizationPersistence organizationPersistence;

    @Mock
    private OrganizationRepository organizationRepository;

    private OrganizationEntity organizationEntity;
    private Organization organization;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = UserEntity.builder()
                .userUuid(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .role(Role.ORGANIZATION)
                .build();

        User user = User.builder()
                .userUuid(userEntity.getUserUuid())
                .email("test@example.com")
                .password("password")
                .role(Role.ORGANIZATION)
                .build();

        organizationEntity = OrganizationEntity.builder()
                .organizationUuid(UUID.randomUUID())
                .user(userEntity)
                .name("Test Organization")
                .phone("123456789")
                .description("Description")
                .website("https://example.com")
                .images("image1.jpg,image2.jpg")
                .build();

        organization = Organization.builder()
                .user(user)
                .organizationUuid(organizationEntity.getOrganizationUuid())
                .name("Test Organization")
                .phone("123456789")
                .description("Description")
                .website("https://example.com")
                .images("image1.jpg,image2.jpg")
                .build();
    }

    @Test
    void testCreateOrganizationSuccess() {
        when(organizationRepository.existsByNameAndPhone(organization.getName(), organization.getPhone())).thenReturn(false);
        when(organizationRepository.save(any(OrganizationEntity.class))).thenReturn(organizationEntity);

        OrganizationEntity createdOrganizationEntity = organizationPersistence.create(organization);
        assertNotNull(createdOrganizationEntity);
        assertEquals(organization.getName(), createdOrganizationEntity.getName());

        verify(organizationRepository, times(1)).save(any(OrganizationEntity.class));
    }

    @Test
    void testCreateOrganizationAlreadyExists() {
        when(organizationRepository.existsByNameAndPhone(organization.getName(), organization.getPhone())).thenReturn(true);

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> organizationPersistence.create(organization));
        assertTrue(thrown.getMessage().contains("Organization already exists"));
    }

    @Test
    void testFindByOrganizationUuidSuccess() {
        when(organizationRepository.findByOrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Optional.of(organizationEntity));

        OrganizationEntity foundOrganizationEntity = organizationPersistence.findByOrganizationUuid(organizationEntity.getOrganizationUuid());
        assertNotNull(foundOrganizationEntity);
        assertEquals(organizationEntity.getName(), foundOrganizationEntity.getName());
    }

    @Test
    void testFindByOrganizationUuidNotFound() {
        when(organizationRepository.findByOrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> organizationPersistence.findByOrganizationUuid(organizationEntity.getOrganizationUuid()));
        assertTrue(thrown.getMessage().contains("Organization not found: " + organizationEntity.getOrganizationUuid()));
    }

    @Test
    void testFindByUserUuidSuccess() {
        when(organizationRepository.findByUser_UserUuid(userEntity.getUserUuid())).thenReturn(Optional.of(organizationEntity));

        OrganizationEntity foundOrganizationEntity = organizationPersistence.findByUserUuid(userEntity.getUserUuid());
        assertNotNull(foundOrganizationEntity);
        assertEquals(organizationEntity.getName(), foundOrganizationEntity.getName());
    }

    @Test
    void testFindByUserUuidNotFound() {
        when(organizationRepository.findByUser_UserUuid(userEntity.getUserUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> organizationPersistence.findByUserUuid(userEntity.getUserUuid()));
        assertTrue(thrown.getMessage().contains("Organization not found for user: " + userEntity.getUserUuid()));
    }

    @Test
    void testUpdateOrganizationSuccess() {
        when(organizationRepository.findByOrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Optional.of(organizationEntity));
        when(organizationRepository.save(any(OrganizationEntity.class))).thenReturn(organizationEntity);

        OrganizationEntity updatedOrganizationEntity = organizationPersistence.update(organizationEntity.getOrganizationUuid(), organization);
        assertNotNull(updatedOrganizationEntity);
        assertEquals(organization.getName(), updatedOrganizationEntity.getName());

        verify(organizationRepository, times(1)).save(any(OrganizationEntity.class));
    }

    @Test
    void testUpdateOrganizationNotFound() {
        when(organizationRepository.findByOrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> organizationPersistence.update(organizationEntity.getOrganizationUuid(), organization));
        assertTrue(thrown.getMessage().contains("Organization not found: " + organizationEntity.getOrganizationUuid()));
    }

    @Test
    void testDeleteOrganizationSuccess() {
        when(organizationRepository.findByOrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Optional.of(organizationEntity));
        doNothing().when(organizationRepository).delete(organizationEntity);

        UUID userUuid = organizationPersistence.delete(organizationEntity.getOrganizationUuid());
        assertNotNull(userUuid);
        assertEquals(userEntity.getUserUuid(), userUuid);

        verify(organizationRepository, times(1)).delete(organizationEntity);
    }

    @Test
    void testDeleteOrganizationNotFound() {
        when(organizationRepository.findByOrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> organizationPersistence.delete(organizationEntity.getOrganizationUuid()));
        assertTrue(thrown.getMessage().contains("Organization not found: " + organizationEntity.getOrganizationUuid()));
    }
}
