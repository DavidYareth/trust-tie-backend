package es.upm.miw.trust_tie_backend.persistence.repositories;

import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
    boolean existsByNameAndPhone(String name, String phone);
}
