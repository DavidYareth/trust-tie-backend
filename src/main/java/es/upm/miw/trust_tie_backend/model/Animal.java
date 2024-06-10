package es.upm.miw.trust_tie_backend.model;

import es.upm.miw.trust_tie_backend.model.dtos.AnimalDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    private UUID animalUuid;
    private Organization organization;
    private String name;
    private String type;
    private String breed;
    private int age;
    private String size;
    private String characteristics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Animal(AnimalDto animalDto) {
        this.name = animalDto.getName();
        this.type = animalDto.getType();
        this.breed = animalDto.getBreed();
        this.age = animalDto.getAge();
        this.size = animalDto.getSize();
        this.characteristics = animalDto.getCharacteristics();
    }

    public Animal(AnimalDto animalDto, UUID animalUuid) {
        this(animalDto);
        this.animalUuid = animalUuid;
    }

}
