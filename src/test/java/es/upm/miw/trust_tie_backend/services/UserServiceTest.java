package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.UnitTestConfig;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.model.dtos.*;
import es.upm.miw.trust_tie_backend.persistence.AdopterPersistence;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@UnitTestConfig
class UserServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password1";
    private static final String TEST_TOKEN = "token";
    private static final String TEST_NOT_EXISTS_EMAIL = "notexists@example.com";
    private static final UUID TEST_UUID = UUID.randomUUID();

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserPersistence userPersistence;

    @MockBean
    private AdopterPersistence adopterPersistence;

    @MockBean
    private OrganizationPersistence organizationPersistence;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        when(userPersistence.findByEmail(anyString())).thenReturn(Optional.of(UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ADOPTER)
                .build()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.createToken(anyString(), anyString())).thenReturn(TEST_TOKEN);
        when(passwordEncoder.encode(anyString())).thenReturn(TEST_PASSWORD);
    }

    @Test
    void testLoginSuccess() {
        LoginDto loginDto = LoginDto.builder().email(TEST_EMAIL).password(TEST_PASSWORD).build();
        TokenDto tokenDto = userService.login(loginDto);
        assertEquals(TEST_TOKEN, tokenDto.getToken());
    }

    @Test
    void testLoginFailurePasswordIncorrect() {
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        LoginDto loginDto = LoginDto.builder().email(TEST_EMAIL).password(TEST_PASSWORD).build();
        assertThrows(BadCredentialsException.class, () -> userService.login(loginDto));
    }

    @Test
    void testLoginFailureUserNotExists() {
        when(userPersistence.findByEmail(anyString())).thenReturn(Optional.empty());
        LoginDto loginDto = LoginDto.builder().email(TEST_NOT_EXISTS_EMAIL).password(TEST_PASSWORD).build();
        assertThrows(BadCredentialsException.class, () -> userService.login(loginDto));
    }

    @Test
    void testRegisterAdopterSuccess() {
        RegisterAdopterDto registerAdopterDto = buildRegisterAdopterDto();
        when(userPersistence.create(any(User.class))).thenReturn(UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ADOPTER)
                .build());

        TokenDto tokenDto = userService.registerAdopter(registerAdopterDto);
        assertEquals(TEST_TOKEN, tokenDto.getToken());
    }

    @Test
    void testRegisterOrganizationSuccess() {
        RegisterOrganizationDto registerOrganizationDto = buildRegisterOrganizationDto();
        when(userPersistence.create(any(User.class))).thenReturn(UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build());

        TokenDto tokenDto = userService.registerOrganization(registerOrganizationDto);
        assertEquals(TEST_TOKEN, tokenDto.getToken());
    }

    @Test
    void testGetAdopterProfileSuccess() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ADOPTER)
                .build();

        AdopterEntity adopterEntity = new AdopterEntity();
        adopterEntity.setUser(userEntity);

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID.toString());
        when(adopterPersistence.findByUserUuid(any(UUID.class))).thenReturn(adopterEntity);

        AdopterDto adopterDto = userService.getAdopterProfile("Bearer " + TEST_TOKEN);
        assertNotNull(adopterDto);
    }

    @Test
    void testGetOrganizationProfileSuccess() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setUser(userEntity);

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID.toString());
        when(organizationPersistence.findByUserUuid(any(UUID.class))).thenReturn(organizationEntity);

        OrganizationDto organizationDto = userService.getOrganizationProfile("Bearer " + TEST_TOKEN);
        assertNotNull(organizationDto);
    }

    private RegisterAdopterDto buildRegisterAdopterDto() {
        return RegisterAdopterDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .firstName("John")
                .lastName("Doe")
                .phone("+123456789")
                .build();
    }

    private RegisterOrganizationDto buildRegisterOrganizationDto() {
        return RegisterOrganizationDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .name("Organization Name")
                .phone("+123456789")
                .build();
    }
}
