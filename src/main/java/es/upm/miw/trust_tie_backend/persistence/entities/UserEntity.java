package es.upm.miw.trust_tie_backend.persistence.entities;

import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @UuidGenerator
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public UserEntity(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public UserEntity(User user, String encodedPassword) {
        this.email = user.getEmail();
        this.password = encodedPassword;
        this.role = user.getRole();
    }
}
