package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Animal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalDto {
    private String animalUuid;
    private String organizationUuid;
    private String name;
    private String type;
    private String breed;
    private int age;
    private String size;
    private String characteristics;

    public AnimalDto(Animal animal) {
        this.animalUuid = animal.getAnimalUuid().toString();
        this.organizationUuid = animal.getOrganization().getOrganizationUuid().toString();
        this.name = animal.getName();
        this.type = animal.getType();
        this.breed = animal.getBreed();
        this.age = animal.getAge();
        this.size = animal.getSize();
        this.characteristics = animal.getCharacteristics();
    }
}
