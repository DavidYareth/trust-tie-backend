package es.upm.miw.trust_tie_backend.model;

import es.upm.miw.trust_tie_backend.model.dtos.OrganizationDto;
import es.upm.miw.trust_tie_backend.model.dtos.RegisterOrganizationDto;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    private User user;
    private UUID organizationUuid;
    private String name;
    private String phone;
    private String description;
    private String website;
    private String images;

    public Organization(RegisterOrganizationDto registerOrganizationDto) {
        this.name = registerOrganizationDto.getName();
        this.phone = registerOrganizationDto.getPhone();
    }

    public Organization(OrganizationDto organizationDto, UUID userUuid) {
        this.user = new User(userUuid, organizationDto.getEmail(), organizationDto.getPassword(), Role.ORGANIZATION);
        this.name = organizationDto.getName();
        this.phone = organizationDto.getPhone();
        this.description = organizationDto.getDescription();
        this.website = organizationDto.getWebsite();
        this.images = organizationDto.getImages();
    }
}
