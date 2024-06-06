package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrganizationPersistence {

    private final OrganizationRepository organizationRepository;

    public OrganizationEntity create(Organization organization) {
        assertOrganizationNotExists(organization.getName(), organization.getPhone());
        return organizationRepository.save(new OrganizationEntity(organization));
    }

    public OrganizationEntity findByOrganizationUuid(UUID organizationUuid) {
        return organizationRepository.findByOrganizationUuid(organizationUuid)
                .orElseThrow(() -> new NotFoundException("Organization not found: " + organizationUuid));
    }

    public OrganizationEntity update(UUID organizationUuid, Organization organization) {
        ensureOrganizationExists(organizationUuid);
        return organizationRepository.save(new OrganizationEntity(organization, organizationUuid));
    }

    public UUID delete(UUID organizationUuid) {
        OrganizationEntity organizationEntity = ensureOrganizationExists(organizationUuid);
        UUID userUuid = organizationEntity.getUser().getUserUuid();
        organizationRepository.delete(organizationEntity);
        return userUuid;
    }

    private void assertOrganizationNotExists(String name, String phone) {
        if (organizationRepository.existsByNameAndPhone(name, phone)) {
            throw new BadRequestException("Organization already exists");
        }
    }

    private OrganizationEntity ensureOrganizationExists(UUID organizationUuid) {
        return organizationRepository.findByOrganizationUuid(organizationUuid)
                .orElseThrow(() -> new NotFoundException("Organization not found: " + organizationUuid));
    }
}
