package es.upm.miw.trust_tie_backend.model;

import es.upm.miw.trust_tie_backend.model.dtos.RegisterOrganizationDto;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    private User user;
    private String name;
    private String phone;
    private String description;
    private String website;
    private String images;

    public Organization(RegisterOrganizationDto registerOrganizationDto) {
        this.name = registerOrganizationDto.getName();
        this.phone = registerOrganizationDto.getPhone();
    }
}
