package es.upm.miw.trust_tie_backend.model;

import es.upm.miw.trust_tie_backend.model.dtos.RegisterDto;
import es.upm.miw.trust_tie_backend.model.dtos.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private Role role;

    public User(RegisterDto registerDto) {
        this.email = registerDto.getEmail();
        this.password = registerDto.getPassword();
        this.role = registerDto.getRole();
    }
}
