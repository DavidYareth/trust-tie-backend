package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.exceptions.UnauthorizedException;
import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.dtos.AdopterDto;
import es.upm.miw.trust_tie_backend.persistence.AdopterPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdopterService {

    private final AdopterPersistence adopterPersistence;
    private final UserPersistence userPersistence;
    private final JwtService jwtService;

    public AdopterDto getAdopter(String adopterUuid) {
        AdopterEntity adopterEntity = adopterPersistence.findByAdopterUuid(UUID.fromString(adopterUuid));
        return new AdopterDto(adopterEntity.toAdopter());
    }

    public AdopterDto updateAdopter(String uuid, AdopterDto adopterDto, String authorization) {
        String token = jwtService.extractToken(authorization);
        verifyAuthorization(token, uuid);
        AdopterEntity adopterEntity = adopterPersistence.update(UUID.fromString(uuid), new Adopter(adopterDto, getUserUuidFromToken(token)));
        return new AdopterDto(adopterEntity.toAdopter());
    }

    @Transactional
    public void deleteAdopter(String adopterUuid, String authorization) {
        String token = jwtService.extractToken(authorization);
        verifyAuthorization(token, adopterUuid);
        UUID userUuid = adopterPersistence.delete(UUID.fromString(adopterUuid));
        userPersistence.delete(userUuid);
    }

    private void verifyAuthorization(String token, String adopterUuid) {
        UUID userUuid = getUserUuidFromToken(token);
        AdopterEntity adopterEntity = adopterPersistence.findByAdopterUuid(UUID.fromString(adopterUuid));
        if (adopterEntity == null || !userUuid.equals(adopterEntity.getUser().getUserUuid())) {
            throw new UnauthorizedException("User not authorized to perform this action");
        }
    }

    private UUID getUserUuidFromToken(String token) {
        return UUID.fromString(jwtService.user(token));
    }
}
