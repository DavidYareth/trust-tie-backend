package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.UserRepository;
import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPersistenceIT {

    @InjectMocks
    private UserPersistence userPersistence;

    @Mock
    private UserRepository userRepository;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = UserEntity.builder()
                .userUuid(UUID.randomUUID())
                .email("test@example.com")
                .password("password")
                .role(Role.ADOPTER)
                .build();

        user = User.builder()
                .userUuid(userEntity.getUserUuid())
                .email("test@example.com")
                .password("password")
                .role(Role.ADOPTER)
                .build();
    }

    @Test
    void testFindByEmailSuccess() {
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        Optional<UserEntity> foundUserEntity = userPersistence.findByEmail(userEntity.getEmail());
        assertTrue(foundUserEntity.isPresent());
        assertEquals(userEntity.getEmail(), foundUserEntity.get().getEmail());
    }

    @Test
    void testFindByEmailNotFound() {
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());
        Optional<UserEntity> foundUserEntity = userPersistence.findByEmail(userEntity.getEmail());
        assertFalse(foundUserEntity.isPresent());
    }

    @Test
    void testCreateUserSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity createdUserEntity = userPersistence.create(user);
        assertNotNull(createdUserEntity);
        assertEquals(user.getEmail(), createdUserEntity.getEmail());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> userPersistence.create(user));
        assertTrue(thrown.getMessage().contains("Email is already in use"));
    }

    @Test
    void testDeleteUserSuccess() {
        doNothing().when(userRepository).deleteByUserUuid(userEntity.getUserUuid());
        userPersistence.delete(userEntity.getUserUuid());
        verify(userRepository, times(1)).deleteByUserUuid(userEntity.getUserUuid());
    }
}
