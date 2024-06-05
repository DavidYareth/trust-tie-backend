package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.OrganizationRepository;
import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrganizationPersistence {

    private final OrganizationRepository organizationRepository;

    public OrganizationEntity create(Organization organization) {
        assertOrganizationNotExists(organization.getName(), organization.getPhone());
        return organizationRepository.save(new OrganizationEntity(organization));
    }

    public void assertOrganizationNotExists(String name, String phone) {
        if (organizationRepository.existsByNameAndPhone(name, phone)) {
            throw new BadRequestException("Organization already exists");
        }
    }
}
