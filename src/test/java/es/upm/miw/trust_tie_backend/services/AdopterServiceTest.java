package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.exceptions.UnauthorizedException;
import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.dtos.AdopterDto;
import es.upm.miw.trust_tie_backend.persistence.AdopterPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdopterServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password1";
    private static final String TEST_TOKEN = "token";
    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final String TEST_UUID_STRING = TEST_UUID.toString();

    @Mock
    private AdopterPersistence adopterPersistence;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AdopterService adopterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAdopter() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ADOPTER)
                .build();

        AdopterEntity adopterEntity = AdopterEntity.builder()
                .adopterUuid(TEST_UUID)
                .user(userEntity)
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();

        when(adopterPersistence.findByAdopterUuid(any(UUID.class))).thenReturn(adopterEntity);

        AdopterDto adopterDto = adopterService.getAdopter(TEST_UUID_STRING);
        assertNotNull(adopterDto);
        assertEquals("John", adopterDto.getFirstName());
        assertEquals("Doe", adopterDto.getLastName());
    }

    @Test
    void testUpdateAdopterSuccess() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ADOPTER)
                .build();

        AdopterEntity adopterEntity = AdopterEntity.builder()
                .adopterUuid(TEST_UUID)
                .user(userEntity)
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();

        AdopterDto adopterDto = AdopterDto.builder()
                .adopterUuid(TEST_UUID_STRING)
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(adopterPersistence.update(any(UUID.class), any(Adopter.class))).thenReturn(adopterEntity);
        when(adopterPersistence.findByAdopterUuid(any(UUID.class))).thenReturn(adopterEntity);

        AdopterDto updatedAdopterDto = adopterService.updateAdopter(TEST_UUID_STRING, adopterDto, "Bearer " + TEST_TOKEN);
        assertNotNull(updatedAdopterDto);
        assertEquals("John", updatedAdopterDto.getFirstName());
        assertEquals("Doe", updatedAdopterDto.getLastName());
    }

    @Test
    void testDeleteAdopterSuccess() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ADOPTER)
                .build();

        AdopterEntity adopterEntity = AdopterEntity.builder()
                .adopterUuid(TEST_UUID)
                .user(userEntity)
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(adopterPersistence.findByAdopterUuid(any(UUID.class))).thenReturn(adopterEntity);

        doNothing().when(userPersistence).delete(any(UUID.class));
        when(adopterPersistence.delete(any(UUID.class))).thenReturn(TEST_UUID);

        adopterService.deleteAdopter(TEST_UUID_STRING, "Bearer " + TEST_TOKEN);

        verify(adopterPersistence, times(1)).delete(TEST_UUID);
        verify(userPersistence, times(1)).delete(TEST_UUID);
    }

    @Test
    void testVerifyAuthorizationFailure() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ADOPTER)
                .build();

        AdopterEntity adopterEntity = AdopterEntity.builder()
                .adopterUuid(TEST_UUID)
                .user(userEntity)
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();

        UUID differentUUID = UUID.randomUUID();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(differentUUID.toString());
        when(adopterPersistence.findByAdopterUuid(any(UUID.class))).thenReturn(adopterEntity);

        assertThrows(UnauthorizedException.class, this::updateAdopterWithException);
    }

    private void updateAdopterWithException() {
        adopterService.updateAdopter(TEST_UUID_STRING, new AdopterDto(), "Bearer " + TEST_TOKEN);
    }
}
