package es.upm.miw.trust_tie_backend.model;

import es.upm.miw.trust_tie_backend.model.dtos.EventDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private UUID eventUuid;
    private Organization organization;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String eventLocation;
    private String images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Event(EventDto eventDto) {
        this.title = eventDto.getTitle();
        this.description = eventDto.getDescription();
        this.eventDate = eventDto.getEventDate();
        this.eventLocation = eventDto.getEventLocation();
        this.images = eventDto.getImages();
    }

    public Event(EventDto eventDto, UUID eventUuid) {
        this(eventDto);
        this.eventUuid = eventUuid;
    }
}
