package es.upm.miw.trust_tie_backend.persistence.repositories;

import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
    boolean existsByNameAndPhone(String name, String phone);
    Optional<OrganizationEntity> findByOrganizationUuid(UUID uuid);
    Optional<OrganizationEntity> findByUser_UserUuid(UUID userUuid);
}
