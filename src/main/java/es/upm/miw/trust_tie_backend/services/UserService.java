package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.model.Adopter;
import es.upm.miw.trust_tie_backend.model.Organization;
import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.dtos.*;
import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.AdopterPersistence;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.AdopterEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        verifyPassword(loginDto.getPassword(), userEntity.getPassword());
        return createToken(userEntity);
    }

    public TokenDto registerAdopter(RegisterAdopterDto registerAdopterDto) {
        UserEntity userEntity = createUser(registerAdopterDto.toUser(), Role.ADOPTER);
        createAdopter(registerAdopterDto, userEntity);
        return createToken(userEntity);
    }

    public TokenDto registerOrganization(RegisterOrganizationDto registerOrganizationDto) {
        UserEntity userEntity = createUser(registerOrganizationDto.toUser(), Role.ORGANIZATION);
        createOrganization(registerOrganizationDto, userEntity);
        return createToken(userEntity);
    }

    public AdopterDto getAdopterProfile(String authorization) {
        String token = jwtService.extractToken(authorization);
        UUID userUuid = getUserUuidFromToken(token);
        AdopterEntity adopterEntity = adopterPersistence.findByUserUuid(userUuid);
        return new AdopterDto(adopterEntity.toAdopter());
    }

    public OrganizationDto getOrganizationProfile(String authorization) {
        String token = jwtService.extractToken(authorization);
        UUID userUuid = getUserUuidFromToken(token);
        OrganizationEntity organizationEntity = organizationPersistence.findByUserUuid(userUuid);
        return new OrganizationDto(organizationEntity.toOrganization());
    }

    private void verifyPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadCredentialsException(INVALID_EMAIL_OR_PASSWORD);
        }
    }

    private TokenDto createToken(UserEntity userEntity) {
        return new TokenDto(jwtService.createToken(userEntity.getUserUuid().toString(), userEntity.getRole().name()));
    }

    private UserEntity createUser(User user, Role role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        return userPersistence.create(user);
    }

    private void createAdopter(RegisterAdopterDto registerAdopterDto, UserEntity userEntity) {
        Adopter adopter = new Adopter(registerAdopterDto);
        adopter.setUser(userEntity.toUser());
        adopterPersistence.create(adopter);
    }

    private void createOrganization(RegisterOrganizationDto registerOrganizationDto, UserEntity userEntity) {
        Organization organization = new Organization(registerOrganizationDto);
        organization.setUser(userEntity.toUser());
        organizationPersistence.create(organization);
    }

    private UUID getUserUuidFromToken(String token) {
        return UUID.fromString(jwtService.user(token));
    }
}
