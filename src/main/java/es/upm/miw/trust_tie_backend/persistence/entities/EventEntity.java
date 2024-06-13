package es.upm.miw.trust_tie_backend.persistence.entities;

import es.upm.miw.trust_tie_backend.model.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private UUID eventUuid;

    @ManyToOne(fetch = FetchType.EAGER)
    private OrganizationEntity organization;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private String eventLocation;

    private String images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public EventEntity(Event event, OrganizationEntity organizationEntity) {
        this.eventUuid = event.getEventUuid();
        this.organization = organizationEntity;
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.eventDate = event.getEventDate();
        this.eventLocation = event.getEventLocation();
        this.images = event.getImages();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getUpdatedAt();
        this.deletedAt = event.getDeletedAt();
    }

    public Event toEvent() {
        return Event.builder()
                .eventUuid(this.eventUuid)
                .organization(this.organization.toOrganization())
                .title(this.title)
                .description(this.description)
                .eventDate(this.eventDate)
                .eventLocation(this.eventLocation)
                .images(this.images)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .build();
    }
}
