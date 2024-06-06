package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.exceptions.UnauthorizedException;
import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.model.dtos.OrganizationDto;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationPersistence organizationPersistence;
    private final UserPersistence userPersistence;
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
        UUID userUuid = organizationPersistence.delete(UUID.fromString(organizationUuid));
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
