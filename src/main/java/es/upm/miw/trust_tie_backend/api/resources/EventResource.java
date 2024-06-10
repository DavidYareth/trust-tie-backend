package es.upm.miw.trust_tie_backend.api.resources;

import es.upm.miw.trust_tie_backend.model.dtos.EventDto;
import es.upm.miw.trust_tie_backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EventResource.EVENTS)
@RequiredArgsConstructor
public class EventResource {

    public static final String EVENTS = "/events";
    public static final String EVENT_UUID = "/{eventUuid}";

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping(EVENT_UUID)
    public EventDto getEvent(@PathVariable String eventUuid) {
        return eventService.getEvent(eventUuid);
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZATION')")
    public EventDto createEvent(@RequestBody EventDto eventDto, @RequestHeader("Authorization") String authorization) {
        return eventService.createEvent(eventDto, authorization);
    }

    @PutMapping(EVENT_UUID)
    @PreAuthorize("hasRole('ORGANIZATION')")
    public EventDto updateEvent(@PathVariable String eventUuid, @RequestBody EventDto eventDto, @RequestHeader("Authorization") String authorization) {
        return eventService.updateEvent(eventUuid, eventDto, authorization);
    }

    @DeleteMapping(EVENT_UUID)
    @PreAuthorize("hasRole('ORGANIZATION')")
    public void deleteEvent(@PathVariable String eventUuid, @RequestHeader("Authorization") String authorization) {
        eventService.deleteEvent(eventUuid, authorization);
    }
}
