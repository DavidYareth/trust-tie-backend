package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Organization;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto {
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")
    private String password;
    private String name;
    private String phone;
    private String description;
    private String website;
    private String images;

    public OrganizationDto(Organization organization) {
        this.email = organization.getUser().getEmail();
        this.name = organization.getName();
        this.phone = organization.getPhone();
        this.description = organization.getDescription();
        this.website = organization.getWebsite();
        this.images = organization.getImages();
    }
}
