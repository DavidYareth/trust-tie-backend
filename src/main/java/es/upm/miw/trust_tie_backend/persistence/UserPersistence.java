package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.UserRepository;
import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import es.upm.miw.trust_tie_backend.exceptions.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistence {

    private final UserRepository userRepository;

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity register(User user, String encodedPassword) {
        assertUserNotExists(user.getEmail());
        return userRepository.save(new UserEntity(user, encodedPassword));
    }

    public void assertUserNotExists(String email) {
        if (findByEmail(email).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }
    }

    public void assertRoleNotAdmin(String role) {
        if ("ADMIN".equals(role)) {
            throw new ForbiddenException("Cannot create an account with ADMIN role");
        }
    }
}
