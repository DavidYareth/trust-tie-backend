package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.UserRepository;
import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserPersistence {

    private final UserRepository userRepository;

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity create(User user) {
        assertUserNotExists(user.getEmail());
        return userRepository.save(new UserEntity(user));
    }

    public void delete(UUID userUuid) {
        userRepository.deleteByUserUuid(userUuid);
    }

    private void assertUserNotExists(String email) {
        if (findByEmail(email).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }
    }
}
