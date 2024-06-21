package es.upm.miw.trust_tie_backend.persistence.repositories;

import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.persistence.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Profile("dev")
public class DatabaseSeeder {

    private static final String DEFAULT_PASSWORD_PROPERTY = "default.password";

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final AdopterRepository adopterRepository;
    private final AnimalRepository animalRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    private String defaultPassword;

    @Autowired
    public DatabaseSeeder(UserRepository userRepository, OrganizationRepository organizationRepository, AdopterRepository adopterRepository, AnimalRepository animalRepository, EventRepository eventRepository, PasswordEncoder passwordEncoder, Environment environment) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.adopterRepository = adopterRepository;
        this.animalRepository = animalRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        initializeDefaultPassword();
        deleteAllAndInitializeAndSeedDataBase();
    }

    private void initializeDefaultPassword() {
        this.defaultPassword = environment.getProperty(DEFAULT_PASSWORD_PROPERTY);
        if (this.defaultPassword == null) {
            throw new IllegalArgumentException("Default password not set in properties");
        }
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBase();
    }

    private void deleteAllAndInitialize() {
        eventRepository.deleteAll();
        animalRepository.deleteAll();
        adopterRepository.deleteAll();
        organizationRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void seedDataBase() {
        UserEntity user1 = userRepository.save(UserEntity.builder()
                .userUuid(UUID.randomUUID())
                .email("org1@example.com")
                .password(passwordEncoder.encode(defaultPassword))
                .role(Role.ORGANIZATION)
                .createdAt(LocalDateTime.now())
                .build());

        UserEntity user2 = userRepository.save(UserEntity.builder()
                .userUuid(UUID.randomUUID())
                .email("org2@example.com")
                .password(passwordEncoder.encode(defaultPassword))
                .role(Role.ORGANIZATION)
                .createdAt(LocalDateTime.now())
                .build());

        UserEntity adopterUser = userRepository.save(UserEntity.builder()
                .userUuid(UUID.randomUUID())
                .email("adopter@example.com")
                .password(passwordEncoder.encode(defaultPassword))
                .role(Role.ADOPTER)
                .createdAt(LocalDateTime.now())
                .build());

        OrganizationEntity organization1 = organizationRepository.save(OrganizationEntity.builder()
                .organizationUuid(UUID.randomUUID())
                .user(user1)
                .name("Animal Shelter One")
                .phone("+123456789")
                .description("A shelter for homeless animals")
                .website("http://www.animalshelterone.com")
                .images("image1.jpg")
                .build());

        organizationRepository.save(OrganizationEntity.builder()
                .organizationUuid(UUID.randomUUID())
                .user(user2)
                .name("Animal Shelter Two")
                .phone("+987654321")
                .description("Another shelter for homeless animals")
                .website("http://www.animalsheltertwo.com")
                .images("image2.jpg")
                .build());

        adopterRepository.save(AdopterEntity.builder()
                .adopterUuid(UUID.randomUUID())
                .user(adopterUser)
                .firstName("John")
                .lastName("Doe")
                .phone("+555123456")
                .biography("Loves animals and wants to adopt.")
                .images("adopter_image.jpg")
                .build());

        animalRepository.save(AnimalEntity.builder()
                .animalUuid(UUID.randomUUID())
                .organization(organization1)
                .name("Buddy")
                .type("Dog")
                .breed("Golden Retriever")
                .age(3)
                .size("Large")
                .characteristics("Friendly and energetic")
                .createdAt(LocalDateTime.now())
                .build());

        animalRepository.save(AnimalEntity.builder()
                .animalUuid(UUID.randomUUID())
                .organization(organization1)
                .name("Mittens")
                .type("Cat")
                .breed("Siamese")
                .age(2)
                .size("Medium")
                .characteristics("Playful and loving")
                .createdAt(LocalDateTime.now())
                .build());

        eventRepository.save(EventEntity.builder()
                .eventUuid(UUID.randomUUID())
                .organization(organization1)
                .title("Adoption Day")
                .description("Come and adopt a pet!")
                .eventDate(LocalDateTime.now().plusDays(7))
                .eventLocation("Animal Shelter One")
                .images("event_image1.jpg")
                .createdAt(LocalDateTime.now())
                .build());

        eventRepository.save(EventEntity.builder()
                .eventUuid(UUID.randomUUID())
                .organization(organization1)
                .title("Charity Run")
                .description("Join us for a charity run to support the shelter.")
                .eventDate(LocalDateTime.now().plusMonths(1))
                .eventLocation("City Park")
                .images("event_image2.jpg")
                .createdAt(LocalDateTime.now())
                .build());
    }
}
