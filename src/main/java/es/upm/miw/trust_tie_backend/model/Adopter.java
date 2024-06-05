package es.upm.miw.trust_tie_backend.model;

import es.upm.miw.trust_tie_backend.model.dtos.AdopterDto;
import es.upm.miw.trust_tie_backend.model.dtos.RegisterAdopterDto;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Adopter {
    private User user;
    private String firstName;
    private String lastName;
    private String phone;
    private String biography;
    private String images;

    public Adopter(RegisterAdopterDto registerAdopterDto) {
        this.firstName = registerAdopterDto.getFirstName();
        this.lastName = registerAdopterDto.getLastName();
        this.phone = registerAdopterDto.getPhone();
    }

    public Adopter(AdopterDto adopterDto, UUID userUuid) {
        this.user = new User(userUuid, adopterDto.getEmail(), adopterDto.getPassword(), Role.ADOPTER);
        this.firstName = adopterDto.getFirstName();
        this.lastName = adopterDto.getLastName();
        this.phone = adopterDto.getPhone();
        this.biography = adopterDto.getBiography();
        this.images = adopterDto.getImages();
    }
}
