package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.exceptions.BadRequestException;
import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.dtos.AdopterDto;
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
        assertAdopterNotExistsByFirstNameAndLastNameAndPhone(adopter.getFirstName(), adopter.getLastName(), adopter.getPhone());
        return adopterRepository.save(new AdopterEntity(adopter));
    }

    public AdopterEntity findByAdopterUuid(UUID adopterUuid) {
        assertAdopterNotExistsByAdopterUuid(adopterUuid);
        return adopterRepository.findByAdopterUuid(adopterUuid);
    }

    public AdopterEntity update(UUID adopterUuid, Adopter adopter) {
        assertAdopterNotExistsByAdopterUuid(adopterUuid);
        return adopterRepository.save(new AdopterEntity(adopter, adopterUuid));
    }

    public void delete(UUID adopterUuid) {
        System.out.println("adopterUuid" + adopterUuid);
        assertAdopterNotExistsByAdopterUuid(adopterUuid);
        adopterRepository.deleteByAdopterUuid(adopterUuid);
    }

    public void assertAdopterNotExistsByFirstNameAndLastNameAndPhone(String firstName, String lastName, String phone) {
        if (adopterRepository.existsByFirstNameAndLastNameAndPhone(firstName, lastName, phone)) {
            throw new BadRequestException("Adopter already exists");
        }
    }

    public void assertAdopterNotExistsByAdopterUuid(UUID adopterUuid) {
        if (!adopterRepository.existsByAdopterUuid(adopterUuid)) {
            throw new NotFoundException("Adopter not found: " + adopterUuid);
        }
    }
}
