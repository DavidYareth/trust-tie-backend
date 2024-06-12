package es.upm.miw.trust_tie_backend.model.dtos;

import es.upm.miw.trust_tie_backend.model.Event;
import es.upm.miw.trust_tie_backend.model.Organization;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventDtoTest {

    @Test
    void testEventDtoConstructor() {
        UUID eventUuid = UUID.randomUUID();
        UUID organizationUuid = UUID.randomUUID();
        Organization organization = Organization.builder().organizationUuid(organizationUuid).build();
        LocalDateTime eventDate = LocalDateTime.now();
        Event event = Event.builder()
                .eventUuid(eventUuid)
                .organization(organization)
                .title("Adoption Event")
                .description("An event for adopting animals")
                .eventDate(eventDate)
                .eventLocation("Central Park")
                .images("event_image.jpg")
                .build();

        EventDto eventDto = new EventDto(event);

        assertEquals(eventUuid.toString(), eventDto.getEventUuid());
        assertEquals(organizationUuid.toString(), eventDto.getOrganizationUuid());
        assertEquals("Adoption Event", eventDto.getTitle());
        assertEquals("An event for adopting animals", eventDto.getDescription());
        assertEquals(eventDate, eventDto.getEventDate());
        assertEquals("Central Park", eventDto.getEventLocation());
        assertEquals("event_image.jpg", eventDto.getImages());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        EventDto eventDto = EventDto.builder()
                .eventUuid(UUID.randomUUID().toString())
                .organizationUuid(UUID.randomUUID().toString())
                .title("Adoption Event")
                .description("An event for adopting animals")
                .eventDate(LocalDateTime.now())
                .eventLocation("Central Park")
                .images("event_image.jpg")
                .build();

        assertNotNull(eventDto);
        assertEquals("Adoption Event", eventDto.getTitle());
        assertEquals("An event for adopting animals", eventDto.getDescription());
        assertEquals("Central Park", eventDto.getEventLocation());
        assertEquals("event_image.jpg", eventDto.getImages());
    }
}
