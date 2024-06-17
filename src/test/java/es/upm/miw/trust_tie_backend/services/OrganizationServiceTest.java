package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.exceptions.UnauthorizedException;
import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.dtos.OrganizationDto;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.AnimalPersistence;
import es.upm.miw.trust_tie_backend.persistence.EventPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password1";
    private static final String TEST_TOKEN = "token";
    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final String TEST_UUID_STRING = TEST_UUID.toString();

    @Mock
    private OrganizationPersistence organizationPersistence;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private AnimalPersistence animalPersistence;

    @Mock
    private EventPersistence eventPersistence;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrganization() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .organizationUuid(TEST_UUID)
                .user(userEntity)
                .name("OrgName")
                .phone("+123456789")
                .build();

        when(organizationPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(organizationEntity);

        OrganizationDto organizationDto = organizationService.getOrganization(TEST_UUID_STRING);
        assertNotNull(organizationDto);
        assertEquals("OrgName", organizationDto.getName());
        assertEquals("+123456789", organizationDto.getPhone());
    }

    @Test
    void testUpdateOrganizationSuccess() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .organizationUuid(TEST_UUID)
                .user(userEntity)
                .name("OrgName")
                .phone("+123456789")
                .build();

        OrganizationDto organizationDto = OrganizationDto.builder()
                .organizationUuid(TEST_UUID_STRING)
                .name("OrgName")
                .phone("+123456789")
                .build();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(organizationPersistence.update(any(UUID.class), any(Organization.class))).thenReturn(organizationEntity);
        when(organizationPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(organizationEntity);

        OrganizationDto updatedOrganizationDto = organizationService.updateOrganization(TEST_UUID_STRING, organizationDto, "Bearer " + TEST_TOKEN);
        assertNotNull(updatedOrganizationDto);
        assertEquals("OrgName", updatedOrganizationDto.getName());
        assertEquals("+123456789", updatedOrganizationDto.getPhone());
    }

    @Test
    void testDeleteOrganizationSuccess() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .organizationUuid(TEST_UUID)
                .user(userEntity)
                .name("OrgName")
                .phone("+123456789")
                .build();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(organizationPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(organizationEntity);
        when(animalPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(Collections.emptyList());
        when(eventPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(Collections.emptyList());

        doNothing().when(userPersistence).delete(any(UUID.class));
        when(organizationPersistence.delete(any(UUID.class))).thenReturn(TEST_UUID);

        organizationService.deleteOrganization(TEST_UUID_STRING, "Bearer " + TEST_TOKEN);

        verify(organizationPersistence, times(1)).delete(TEST_UUID);
        verify(userPersistence, times(1)).delete(TEST_UUID);
    }

    @Test
    void testVerifyAuthorizationFailure() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .organizationUuid(TEST_UUID)
                .user(userEntity)
                .name("OrgName")
                .phone("+123456789")
                .build();

        UUID differentUUID = UUID.randomUUID();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(differentUUID.toString());
        when(organizationPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(organizationEntity);

        assertThrows(UnauthorizedException.class, () -> organizationService.updateOrganization(TEST_UUID_STRING, new OrganizationDto(), "Bearer " + TEST_TOKEN));
    }
}
