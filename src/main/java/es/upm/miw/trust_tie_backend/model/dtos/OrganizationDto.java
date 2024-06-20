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
    private String organizationUuid;
    @Pattern(regexp = "^[a-z\\d._%+-]+@[a-z\\d.-]+\\.[a-z]{2,4}$")
    private String email;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$")
    private String password;
    private String name;
    @Pattern(regexp = "^(\\+\\d{1,15})$")
    private String phone;
    private String description;
    private String website;
    private String images;

    public OrganizationDto(Organization organization) {
        this.organizationUuid = String.valueOf(organization.getOrganizationUuid());
        this.email = organization.getUser().getEmail();
        this.name = organization.getName();
        this.phone = organization.getPhone();
        this.description = organization.getDescription();
        this.website = organization.getWebsite();
        this.images = organization.getImages();
    }
}
