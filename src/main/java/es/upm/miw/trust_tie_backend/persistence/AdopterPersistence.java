package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.AdopterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AdopterPersistence {

    private final AdopterRepository adopterRepository;

    public AdopterEntity create(Adopter adopter) {
        ensureAdopterDoesNotExistByDetails(adopter.getFirstName(), adopter.getLastName(), adopter.getPhone());
        return adopterRepository.save(new AdopterEntity(adopter));
    }

    public AdopterEntity findByAdopterUuid(UUID adopterUuid) {
        return adopterRepository.findByAdopterUuid(adopterUuid)
                .orElseThrow(() -> new NotFoundException("Adopter not found: " + adopterUuid));
    }

    public AdopterEntity update(UUID adopterUuid, Adopter adopter) {
        ensureAdopterExists(adopterUuid);
        return adopterRepository.save(new AdopterEntity(adopter, adopterUuid));
    }

    public UUID delete(UUID adopterUuid) {
        AdopterEntity adopterEntity = ensureAdopterExists(adopterUuid);
        UUID userUuid = adopterEntity.getUser().getUserUuid();
        adopterRepository.delete(adopterEntity);
        return userUuid;
    }

    private void ensureAdopterDoesNotExistByDetails(String firstName, String lastName, String phone) {
        if (adopterRepository.existsByFirstNameAndLastNameAndPhone(firstName, lastName, phone)) {
            throw new BadRequestException("Adopter already exists");
        }
    }

    private AdopterEntity ensureAdopterExists(UUID adopterUuid) {
        return adopterRepository.findByAdopterUuid(adopterUuid)
                .orElseThrow(() -> new NotFoundException("Adopter not found: " + adopterUuid));
    }
}
