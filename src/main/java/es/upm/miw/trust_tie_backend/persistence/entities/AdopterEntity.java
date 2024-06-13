package es.upm.miw.trust_tie_backend.persistence.entities;

import es.upm.miw.trust_tie_backend.model.Adopter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "adopters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"firstName", "lastName", "phone"})
})
public class AdopterEntity {
    @Id
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID adopterUuid;

    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    private String biography;
    private String images;

    public AdopterEntity(Adopter adopter) {
        this.user = new UserEntity(adopter.getUser());
        this.firstName = adopter.getFirstName();
        this.lastName = adopter.getLastName();
        this.phone = adopter.getPhone();
        this.biography = adopter.getBiography();
        this.images = adopter.getImages();
    }

    public AdopterEntity(Adopter adopter, UUID adopterUuid) {
        this(adopter);
        this.adopterUuid = adopterUuid;
    }

    public Adopter toAdopter() {
        return new Adopter(this.user.toUser(), this.firstName, this.lastName, this.phone, this.biography, this.images);
    }
}
