package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Adopter;
import jakarta.validation.constraints.NotBlank;
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
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String biography;
    private String images;

    public AdopterDto(Adopter adopter) {
        this.email = adopter.getUser().getEmail();
        this.firstName = adopter.getFirstName();
        this.lastName = adopter.getLastName();
        this.phone = adopter.getPhone();
        this.biography = adopter.getBiography();
        this.images = adopter.getImages();
    }
}
