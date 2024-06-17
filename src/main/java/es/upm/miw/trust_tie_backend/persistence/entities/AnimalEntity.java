package es.upm.miw.trust_tie_backend.persistence.entities;

import es.upm.miw.trust_tie_backend.model.Animal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "animals")
public class AnimalEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID animalUuid;

    @ManyToOne(fetch = FetchType.EAGER)
    private OrganizationEntity organization;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String size;

    private String characteristics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public AnimalEntity(Animal animal, OrganizationEntity organizationEntity) {
        this.animalUuid = animal.getAnimalUuid();
        this.organization = organizationEntity;
        this.name = animal.getName();
        this.type = animal.getType();
        this.breed = animal.getBreed();
        this.age = animal.getAge();
        this.size = animal.getSize();
        this.characteristics = animal.getCharacteristics();
        this.createdAt = animal.getCreatedAt();
        this.updatedAt = animal.getUpdatedAt();
        this.deletedAt = animal.getDeletedAt();
    }

    public Animal toAnimal() {
        return Animal.builder()
                .animalUuid(this.animalUuid)
                .organization(this.organization.toOrganization())
                .name(this.name)
                .type(this.type)
                .breed(this.breed)
                .age(this.age)
                .size(this.size)
                .characteristics(this.characteristics)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .build();
    }
}
