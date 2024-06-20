package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Adopter;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdopterDto {
    private String adopterUuid;
    @Pattern(regexp = "^[a-z\\d._%+-]+@[a-z\\d.-]+\\.[a-z]{2,4}$")
    private String email;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")
    private String password;
    private String firstName;
    private String lastName;
    @Pattern(regexp = "^(\\+\\d{1,15})$")
    private String phone;
    private String biography;
    private String images;

    public AdopterDto(Adopter adopter) {
        this.adopterUuid = String.valueOf(adopter.getAdopterUuid());
        this.email = adopter.getUser().getEmail();
        this.firstName = adopter.getFirstName();
        this.lastName = adopter.getLastName();
        this.phone = adopter.getPhone();
        this.biography = adopter.getBiography();
        this.images = adopter.getImages();
    }
}
