package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.model.Animal;
import es.upm.miw.trust_tie_backend.model.dtos.AnimalDto;
import es.upm.miw.trust_tie_backend.persistence.AnimalPersistence;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AnimalEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalPersistence animalPersistence;
    private final OrganizationPersistence organizationPersistence;
    private final JwtService jwtService;

    public List<AnimalDto> getAllAnimals() {
        return animalPersistence.findAll().stream()
                .map(AnimalEntity::toAnimal)
                .map(AnimalDto::new)
                .collect(Collectors.toList());
    }

    public AnimalDto getAnimal(String animalUuid) {
        AnimalEntity animalEntity = animalPersistence.findByUuid(UUID.fromString(animalUuid));
        Animal animal = animalEntity.toAnimal();
        return new AnimalDto(animal);
    }

    @Transactional
    public AnimalDto createAnimal(AnimalDto animalDto, String authorization) {
        UUID userUuid = getUserUuidFromToken(authorization);
        OrganizationEntity organizationEntity = organizationPersistence.findByUserUuid(userUuid);
        Animal animal = new Animal(animalDto);
        animal.setOrganization(organizationEntity.toOrganization());
        AnimalEntity animalEntity = new AnimalEntity(animal, organizationEntity);
        AnimalEntity createdAnimalEntity = animalPersistence.create(animalEntity);
        return new AnimalDto(createdAnimalEntity.toAnimal());
    }

    @Transactional
    public AnimalDto updateAnimal(String animalUuid, AnimalDto animalDto, String authorization) {
        UUID userUuid = getUserUuidFromToken(authorization);
        AnimalEntity existingAnimalEntity = animalPersistence.findByUuid(UUID.fromString(animalUuid));
        Animal existingAnimal = existingAnimalEntity.toAnimal();
        checkUserAuthorization(existingAnimal, userUuid);
        Animal animal = new Animal(animalDto, UUID.fromString(animalUuid));
        animal.setOrganization(existingAnimal.getOrganization());
        AnimalEntity updatedAnimalEntity = animalPersistence.update(new AnimalEntity(animal, existingAnimalEntity.getOrganization()));
        return new AnimalDto(updatedAnimalEntity.toAnimal());
    }

    @Transactional
    public void deleteAnimal(String animalUuid, String authorization) {
        UUID userUuid = getUserUuidFromToken(authorization);
        AnimalEntity existingAnimalEntity = animalPersistence.findByUuid(UUID.fromString(animalUuid));
        Animal existingAnimal = existingAnimalEntity.toAnimal();
        checkUserAuthorization(existingAnimal, userUuid);
        animalPersistence.delete(UUID.fromString(animalUuid));
    }

    private void checkUserAuthorization(Animal animal, UUID userUuid) {
        if (!animal.getOrganization().getUser().getUserUuid().equals(userUuid)) {
            throw new SecurityException("Access Denied: Animal does not belong to the organization");
        }
    }

    private UUID getUserUuidFromToken(String authorization) {
        String token = jwtService.extractToken(authorization);
        return UUID.fromString(jwtService.user(token));
    }
}
