package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrganizationDtoTest {

    @Test
    void testOrganizationDtoConstructor() {
        User user = new User("org@example.com", "Password1", Role.ORGANIZATION);
        Organization organization = Organization.builder()
                .organizationUuid(UUID.randomUUID())
                .user(user)
                .name("Test Organization")
                .phone("123456789")
                .description("A test organization")
                .website("http://example.com")
                .images("org_image.jpg")
                .build();

        OrganizationDto organizationDto = new OrganizationDto(organization);

        assertEquals(organization.getOrganizationUuid().toString(), organizationDto.getOrganizationUuid());
        assertEquals("org@example.com", organizationDto.getEmail());
        assertEquals("Test Organization", organizationDto.getName());
        assertEquals("123456789", organizationDto.getPhone());
        assertEquals("A test organization", organizationDto.getDescription());
        assertEquals("http://example.com", organizationDto.getWebsite());
        assertEquals("org_image.jpg", organizationDto.getImages());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        OrganizationDto organizationDto = OrganizationDto.builder()
                .organizationUuid(UUID.randomUUID().toString())
                .email("builder_org@example.com")
                .password("Password1")
                .name("Builder Organization")
                .phone("987654321")
                .description("Builder's organization description")
                .website("http://builder.com")
                .images("builder_org_image.jpg")
                .build();

        assertNotNull(organizationDto);
        assertEquals("builder_org@example.com", organizationDto.getEmail());
        assertEquals("Password1", organizationDto.getPassword());
        assertEquals("Builder Organization", organizationDto.getName());
        assertEquals("987654321", organizationDto.getPhone());
        assertEquals("Builder's organization description", organizationDto.getDescription());
        assertEquals("http://builder.com", organizationDto.getWebsite());
        assertEquals("builder_org_image.jpg", organizationDto.getImages());
    }
}
