package es.upm.miw.trust_tie_backend.persistence.entities;

import es.upm.miw.trust_tie_backend.model.Organization;
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
@Table(name = "organizations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "phone"})
})
public class OrganizationEntity {
    @Id
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID organizationUuid;

    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String description;
    private String website;
    private String images;

    public OrganizationEntity(Organization organization) {
        this.user = new UserEntity(organization.getUser());
        this.name = organization.getName();
        this.phone = organization.getPhone();
        this.description = organization.getDescription();
        this.website = organization.getWebsite();
        this.images = organization.getImages();
    }

    public OrganizationEntity(Organization organization, UUID organizationUuid) {
        this(organization);
        this.organizationUuid = organizationUuid;
    }

    public Organization toOrganization() {
        return new Organization(this.user.toUser(), this.organizationUuid, this.name, this.phone, this.description, this.website, this.images);
    }
}
