package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.dtos.AnimalDto;
import es.upm.miw.trust_tie_backend.persistence.AnimalPersistence;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AnimalEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AnimalServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password1";
    private static final String TEST_TOKEN = "token";
    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final String TEST_UUID_STRING = TEST_UUID.toString();

    @Mock
    private AnimalPersistence animalPersistence;

    @Mock
    private OrganizationPersistence organizationPersistence;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AnimalService animalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAnimals() {
        AnimalEntity animalEntity = createAnimalEntity();
        when(animalPersistence.findAll()).thenReturn(Stream.of(animalEntity).collect(Collectors.toList()));

        List<AnimalDto> animalDtos = animalService.getAllAnimals();
        assertNotNull(animalDtos);
        assertEquals(1, animalDtos.size());
        assertEquals("Rex", animalDtos.get(0).getName());
    }

    @Test
    void testGetAnimal() {
        AnimalEntity animalEntity = createAnimalEntity();
        when(animalPersistence.findByUuid(any(UUID.class))).thenReturn(animalEntity);

        AnimalDto animalDto = animalService.getAnimal(TEST_UUID_STRING);
        assertNotNull(animalDto);
        assertEquals("Rex", animalDto.getName());
    }

    @Test
    void testGetAnimalsByOrganization() {
        AnimalEntity animalEntity = createAnimalEntity();
        when(animalPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(Stream.of(animalEntity).collect(Collectors.toList()));

        List<AnimalDto> animalDtos = animalService.getAnimalsByOrganization(TEST_UUID);
        assertNotNull(animalDtos);
        assertEquals(1, animalDtos.size());
        assertEquals("Rex", animalDtos.get(0).getName());
    }

    @Test
    void testGetMyAnimals() {
        OrganizationEntity organizationEntity = createOrganizationEntity();
        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(organizationPersistence.findByUserUuid(any(UUID.class))).thenReturn(organizationEntity);
        when(animalPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(Stream.of(createAnimalEntity()).collect(Collectors.toList()));

        List<AnimalDto> animalDtos = animalService.getMyAnimals("Bearer " + TEST_TOKEN);
        assertNotNull(animalDtos);
        assertEquals(1, animalDtos.size());
        assertEquals("Rex", animalDtos.get(0).getName());
    }

    @Test
    void testCreateAnimal() {
        OrganizationEntity organizationEntity = createOrganizationEntity();
        AnimalDto animalDto = createAnimalDto();
        AnimalEntity animalEntity = createAnimalEntity();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(organizationPersistence.findByUserUuid(any(UUID.class))).thenReturn(organizationEntity);
        when(animalPersistence.create(any(AnimalEntity.class))).thenReturn(animalEntity);

        AnimalDto createdAnimalDto = animalService.createAnimal(animalDto, "Bearer " + TEST_TOKEN);
        assertNotNull(createdAnimalDto);
        assertEquals("Rex", createdAnimalDto.getName());
    }

    @Test
    void testUpdateAnimal() {
        AnimalDto animalDto = createAnimalDto();
        AnimalEntity animalEntity = createAnimalEntity();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(animalPersistence.findByUuid(any(UUID.class))).thenReturn(animalEntity);
        when(animalPersistence.update(any(AnimalEntity.class))).thenReturn(animalEntity);

        AnimalDto updatedAnimalDto = animalService.updateAnimal(TEST_UUID_STRING, animalDto, "Bearer " + TEST_TOKEN);
        assertNotNull(updatedAnimalDto);
        assertEquals("Rex", updatedAnimalDto.getName());
    }

    @Test
    void testDeleteAnimal() {
        AnimalEntity animalEntity = createAnimalEntity();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(animalPersistence.findByUuid(any(UUID.class))).thenReturn(animalEntity);

        doNothing().when(animalPersistence).delete(any(UUID.class));

        animalService.deleteAnimal(TEST_UUID_STRING, "Bearer " + TEST_TOKEN);

        verify(animalPersistence, times(1)).delete(TEST_UUID);
    }

    private AnimalEntity createAnimalEntity() {
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

        return AnimalEntity.builder()
                .animalUuid(TEST_UUID)
                .organization(organizationEntity)
                .name("Rex")
                .type("Dog")
                .breed("German Shepherd")
                .age(5)
                .size("Large")
                .characteristics("Friendly")
                .build();
    }

    private OrganizationEntity createOrganizationEntity() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        return OrganizationEntity.builder()
                .organizationUuid(TEST_UUID)
                .user(userEntity)
                .name("OrgName")
                .phone("+123456789")
                .build();
    }

    private AnimalDto createAnimalDto() {
        return AnimalDto.builder()
                .animalUuid(TEST_UUID_STRING)
                .name("Rex")
                .type("Dog")
                .breed("German Shepherd")
                .age(5)
                .size("Large")
                .characteristics("Friendly")
                .build();
    }
}
