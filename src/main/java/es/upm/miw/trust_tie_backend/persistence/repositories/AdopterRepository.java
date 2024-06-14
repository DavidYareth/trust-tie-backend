package es.upm.miw.trust_tie_backend.persistence.repositories;

import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdopterRepository extends JpaRepository<AdopterEntity, Long> {
    boolean existsByFirstNameAndLastNameAndPhone(String firstName, String lastName, String phone);
    Optional<AdopterEntity> findByAdopterUuid(UUID uuid);
    Optional<AdopterEntity> findByUser_UserUuid(UUID userUuid);
}
