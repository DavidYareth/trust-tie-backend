package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.exceptions.UnauthorizedException;
import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.model.dtos.OrganizationDto;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.AnimalPersistence;
import es.upm.miw.trust_tie_backend.persistence.EventPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AnimalEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.EventEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationPersistence organizationPersistence;
    private final UserPersistence userPersistence;
    private final AnimalPersistence animalPersistence;
    private final EventPersistence eventPersistence;
    private final JwtService jwtService;

    public OrganizationDto getOrganization(String organizationUuid) {
        OrganizationEntity organizationEntity = organizationPersistence.findByOrganizationUuid(UUID.fromString(organizationUuid));
        return new OrganizationDto(organizationEntity.toOrganization());
    }

    public OrganizationDto updateOrganization(String uuid, OrganizationDto organizationDto, String authorization) {
        String token = jwtService.extractToken(authorization);
        verifyAuthorization(token, uuid);
        OrganizationEntity organizationEntity = organizationPersistence.update(UUID.fromString(uuid), new Organization(organizationDto, getUserUuidFromToken(token)));
        return new OrganizationDto(organizationEntity.toOrganization());
    }

    @Transactional
    public void deleteOrganization(String organizationUuid, String authorization) {
        String token = jwtService.extractToken(authorization);
        verifyAuthorization(token, organizationUuid);

        UUID orgUuid = UUID.fromString(organizationUuid);

        // Delete related animals
        List<AnimalEntity> animals = animalPersistence.findByOrganizationUuid(orgUuid);
        animals.forEach(animal -> animalPersistence.delete(animal.getAnimalUuid()));

        // Delete related events
        List<EventEntity> events = eventPersistence.findByOrganizationUuid(orgUuid);
        events.forEach(event -> eventPersistence.delete(event.getEventUuid()));

        // Delete the organization
        UUID userUuid = organizationPersistence.delete(orgUuid);
        userPersistence.delete(userUuid);
    }

    private void verifyAuthorization(String token, String organizationUuid) {
        UUID userUuid = getUserUuidFromToken(token);
        OrganizationEntity organizationEntity = organizationPersistence.findByOrganizationUuid(UUID.fromString(organizationUuid));
        if (organizationEntity == null || !userUuid.equals(organizationEntity.getUser().getUserUuid())) {
            throw new UnauthorizedException("User not authorized to perform this action");
        }
    }

    private UUID getUserUuidFromToken(String token) {
        return UUID.fromString(jwtService.user(token));
    }
}
