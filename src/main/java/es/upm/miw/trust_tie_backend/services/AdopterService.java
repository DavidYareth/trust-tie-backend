package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.exceptions.UnauthorizedException;
import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.dtos.AdopterDto;
import es.upm.miw.trust_tie_backend.persistence.AdopterPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdopterService {

    private final AdopterPersistence adopterPersistence;
    private final JwtService jwtService;

    public AdopterDto getAdopter(String adopterUuid) {
        UUID uuid = UUID.fromString(adopterUuid);
        AdopterEntity adopterEntity = adopterPersistence.findByAdopterUuid(uuid);
        return new AdopterDto(adopterEntity.toAdopter());
    }

    public AdopterDto updateAdopter(String uuid, AdopterDto adopterDto, String authorization) {
        String token = jwtService.extractToken(authorization);
        if (isUnauthorizedAdopterUuid(token, uuid)) {
            throw new UnauthorizedException("User not authorized to update this adopter");
        }
        UUID adopterUuid = UUID.fromString(uuid);
        UUID userUuid = UUID.fromString(jwtService.user(token));
        AdopterEntity adopterEntity = adopterPersistence.update(adopterUuid, new Adopter(adopterDto, userUuid));
        return new AdopterDto(adopterEntity.toAdopter());
    }

    @Transactional
    public void deleteAdopter(String adopterUuid, String authorization) {
        String token = jwtService.extractToken(authorization);
        if (isUnauthorizedAdopterUuid(token, adopterUuid)) {
            throw new UnauthorizedException("User not authorized to delete this adopter");
        }
        adopterPersistence.delete(UUID.fromString(adopterUuid));
    }

    public boolean isUnauthorizedAdopterUuid(String token, String adopterUuid) {
        UUID userUuid = UUID.fromString(jwtService.user(token));
        AdopterEntity adopterEntity = adopterPersistence.findByAdopterUuid(UUID.fromString(adopterUuid));
        return adopterEntity == null || !userUuid.equals(adopterEntity.getUser().getUserUuid());
    }
}
