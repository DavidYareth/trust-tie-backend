package es.upm.miw.trust_tie_backend.api.resources;

import es.upm.miw.trust_tie_backend.model.dtos.OrganizationDto;
import es.upm.miw.trust_tie_backend.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(OrganizationResource.ORGANIZATION)
@RequiredArgsConstructor
public class OrganizationResource {

    public static final String ORGANIZATION = "/organizations";
    public static final String ORGANIZATION_UUID = "/{uuid}";

    private final OrganizationService organizationService;

    @GetMapping(ORGANIZATION_UUID)
    public OrganizationDto getOrganization(@PathVariable String uuid) {
        return organizationService.getOrganization(uuid);
    }

    @PutMapping(ORGANIZATION_UUID)
    @PreAuthorize("hasRole('ORGANIZATION')")
    public OrganizationDto updateOrganization(@PathVariable String uuid, @RequestBody OrganizationDto organizationDto, @RequestHeader("Authorization") String authorization) {
        return organizationService.updateOrganization(uuid, organizationDto, authorization);
    }

    @DeleteMapping(ORGANIZATION_UUID)
    @PreAuthorize("hasRole('ORGANIZATION')")
    public void deleteOrganization(@PathVariable String uuid, @RequestHeader("Authorization") String authorization) {
        organizationService.deleteOrganization(uuid, authorization);
    }
}
