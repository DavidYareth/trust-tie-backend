package es.upm.miw.trust_tie_backend.api.resources;

import es.upm.miw.trust_tie_backend.model.dtos.LoginDto;
import es.upm.miw.trust_tie_backend.model.dtos.RegisterDto;
import es.upm.miw.trust_tie_backend.model.dtos.TokenDto;
import es.upm.miw.trust_tie_backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthResource.AUTH)
@RequiredArgsConstructor
public class AuthResource {

    public static final String AUTH = "/auth";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";

    private final UserService userService;

    @PostMapping(AuthResource.LOGIN)
    public TokenDto login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping(AuthResource.REGISTER)
    public TokenDto register(@Valid @RequestBody RegisterDto registerDto) {
        return userService.register(registerDto);
    }
}
