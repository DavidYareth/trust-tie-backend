package es.upm.miw.trust_tie_backend.api.resources;

import es.upm.miw.trust_tie_backend.model.dtos.AdopterDto;
import es.upm.miw.trust_tie_backend.services.AdopterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AdopterResource.ADOPTER)
@RequiredArgsConstructor
public class AdopterResource {

    public static final String ADOPTER = "/adopters";
    public static final String ADOPTER_UUID = "/{uuid}";

    private final AdopterService adopterService;

    @GetMapping(ADOPTER_UUID)
    public AdopterDto getAdopter(@PathVariable String uuid) {
        return adopterService.getAdopter(uuid);
    }

    @PutMapping(ADOPTER_UUID)
    @PreAuthorize("hasRole('ADOPTER')")
    public AdopterDto updateAdopter(@PathVariable String uuid, @RequestBody AdopterDto adopterDto, @RequestHeader("Authorization") String authorization) {
        return adopterService.updateAdopter(uuid, adopterDto, authorization);
    }

    @DeleteMapping(ADOPTER_UUID)
    @PreAuthorize("hasRole('ADOPTER')")
    public void deleteAdopter(@PathVariable String uuid, @RequestHeader("Authorization") String authorization) {
        adopterService.deleteAdopter(uuid, authorization);
    }
}
