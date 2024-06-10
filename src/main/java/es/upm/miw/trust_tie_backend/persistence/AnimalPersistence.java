package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.persistence.entities.AnimalEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.AnimalRepository;
import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AnimalPersistence {

    private final AnimalRepository animalRepository;

    public List<AnimalEntity> findAll() {
        return animalRepository.findAll();
    }

    public AnimalEntity findByUuid(UUID animalUuid) {
        return animalRepository.findById(animalUuid)
                .orElseThrow(() -> new NotFoundException("Animal not found: " + animalUuid));
    }

    public AnimalEntity create(AnimalEntity animalEntity) {
        return animalRepository.save(animalEntity);
    }

    public AnimalEntity update(AnimalEntity animalEntity) {
        assertAnimalExists(animalEntity.getAnimalUuid());
        return animalRepository.save(animalEntity);
    }

    public void delete(UUID animalUuid) {
        assertAnimalExists(animalUuid);
        animalRepository.deleteById(animalUuid);
    }

    private void assertAnimalExists(UUID animalUuid) {
        if (!animalRepository.existsById(animalUuid)) {
            throw new NotFoundException("Animal not found: " + animalUuid);
        }
    }
}
