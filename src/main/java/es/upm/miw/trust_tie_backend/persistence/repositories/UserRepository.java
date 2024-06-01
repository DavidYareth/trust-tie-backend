package es.upm.miw.trust_tie_backend.persistence.repositories;

import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
