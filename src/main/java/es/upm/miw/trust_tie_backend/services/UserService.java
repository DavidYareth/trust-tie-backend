package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.model.dtos.LoginDto;
import es.upm.miw.trust_tie_backend.model.dtos.RegisterDto;
import es.upm.miw.trust_tie_backend.model.dtos.TokenDto;
import es.upm.miw.trust_tie_backend.model.User;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import es.upm.miw.trust_tie_backend.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";

    private final UserPersistence userPersistence;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenDto register(RegisterDto registerDto) {
        userPersistence.assertRoleNotAdmin(registerDto.getRole().name());
        User user = new User(registerDto);
        UserEntity userEntity = userPersistence.register(user, passwordEncoder.encode(user.getPassword()));

        return new TokenDto(jwtService.createToken(userEntity.getEmail(), userEntity.getRole().name()));
    }

    public TokenDto login(LoginDto loginDto) {
        UserEntity userEntity = userPersistence.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException(INVALID_EMAIL_OR_PASSWORD));
        if (!passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException(INVALID_EMAIL_OR_PASSWORD);
        }
        return new TokenDto(jwtService.createToken(userEntity.getEmail(), userEntity.getRole().name()));
    }
}
