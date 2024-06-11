package es.upm.miw.trust_tie_backend.persistence.repositories;

import es.upm.miw.trust_tie_backend.persistence.entities.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnimalRepository extends JpaRepository<AnimalEntity, UUID> {
    List<AnimalEntity> findByOrganization_OrganizationUuid(UUID organizationUuid);
}
