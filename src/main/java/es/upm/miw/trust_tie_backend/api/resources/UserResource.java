package es.upm.miw.trust_tie_backend.api.resources;

import es.upm.miw.trust_tie_backend.model.dtos.*;
import es.upm.miw.trust_tie_backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserResource.USER)
@RequiredArgsConstructor
public class UserResource {

    public static final String USER = "/users";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String ADOPTER = "/adopter";
    public static final String ORGANIZATION = "/organization";

    private final UserService userService;

    @PostMapping(LOGIN)
    public TokenDto login(@Valid @RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping(REGISTER + ADOPTER)
    public TokenDto registerAdopter(@Valid @RequestBody RegisterAdopterDto registerAdopterDto) {
        return userService.registerAdopter(registerAdopterDto);
    }

    @PostMapping(REGISTER + ORGANIZATION)
    public TokenDto registerOrganization(@Valid @RequestBody RegisterOrganizationDto registerOrganizationDto) {
        return userService.registerOrganization(registerOrganizationDto);
    }
}
