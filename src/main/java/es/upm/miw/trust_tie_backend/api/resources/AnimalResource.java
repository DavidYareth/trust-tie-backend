package es.upm.miw.trust_tie_backend.api.resources;

import es.upm.miw.trust_tie_backend.model.dtos.AnimalDto;
import es.upm.miw.trust_tie_backend.services.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AnimalResource.ANIMALS)
@RequiredArgsConstructor
public class AnimalResource {

    public static final String ANIMALS = "/animals";
    public static final String ANIMAL_UUID = "/{animalUuid}";

    private final AnimalService animalService;

    @GetMapping
    public List<AnimalDto> getAllAnimals() {
        return animalService.getAllAnimals();
    }

    @GetMapping(ANIMAL_UUID)
    public AnimalDto getAnimal(@PathVariable String animalUuid) {
        return animalService.getAnimal(animalUuid);
    }

    @PostMapping
    public AnimalDto createAnimal(@RequestBody AnimalDto animalDto, @RequestHeader("Authorization") String authorization) {
        return animalService.createAnimal(animalDto, authorization);
    }

    @PutMapping(ANIMAL_UUID)
    public AnimalDto updateAnimal(@PathVariable String animalUuid, @RequestBody AnimalDto animalDto, @RequestHeader("Authorization") String authorization) {
        return animalService.updateAnimal(animalUuid, animalDto, authorization);
    }

    @DeleteMapping(ANIMAL_UUID)
    public void deleteAnimal(@PathVariable String animalUuid, @RequestHeader("Authorization") String authorization) {
        animalService.deleteAnimal(animalUuid, authorization);
    }
}
