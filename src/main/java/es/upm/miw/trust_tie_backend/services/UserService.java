package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.dtos.*;
import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.AdopterPersistence;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";

    private final UserPersistence userPersistence;
    private final AdopterPersistence adopterPersistence;
    private final OrganizationPersistence organizationPersistence;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenDto login(LoginDto loginDto) {
        UserEntity userEntity = userPersistence.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException(INVALID_EMAIL_OR_PASSWORD));
        if (!passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException(INVALID_EMAIL_OR_PASSWORD);
        }
        return new TokenDto(jwtService.createToken(userEntity.getUserUuid().toString(), userEntity.getRole().name()));
    }

    public TokenDto registerAdopter(RegisterAdopterDto registerAdopterDto) {
        User user = registerAdopterDto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADOPTER);
        UserEntity userEntity = userPersistence.create(user);

        Adopter adopter = new Adopter(registerAdopterDto);
        adopter.setUser(userEntity.toUser());
        adopterPersistence.create(adopter);

        return new TokenDto(jwtService.createToken(userEntity.getUserUuid().toString(), userEntity.getRole().name()));
    }

    public TokenDto registerOrganization(RegisterOrganizationDto registerOrganizationDto) {
        User user = registerOrganizationDto.toUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ORGANIZATION);
        UserEntity userEntity = userPersistence.create(user);

        Organization organization = new Organization(registerOrganizationDto);
        organization.setUser(userEntity.toUser());
        organizationPersistence.create(organization);

        return new TokenDto(jwtService.createToken(userEntity.getUserUuid().toString(), userEntity.getRole().name()));
    }
}
