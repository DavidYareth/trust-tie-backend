package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String eventUuid;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String eventLocation;
    private String images;

    public EventDto(Event event) {
        this.eventUuid = event.getEventUuid().toString();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.eventDate = event.getEventDate();
        this.eventLocation = event.getEventLocation();
        this.images = event.getImages();
    }
}
