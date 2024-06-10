package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.model.Event;
import es.upm.miw.trust_tie_backend.model.dtos.EventDto;
import es.upm.miw.trust_tie_backend.persistence.EventPersistence;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.EventEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventPersistence eventPersistence;
    private final OrganizationPersistence organizationPersistence;
    private final JwtService jwtService;

    public List<EventDto> getAllEvents() {
        return eventPersistence.findAll().stream()
                .map(EventEntity::toEvent)
                .map(EventDto::new)
                .collect(Collectors.toList());
    }

    public EventDto getEvent(String eventUuid) {
        EventEntity eventEntity = eventPersistence.findByUuid(UUID.fromString(eventUuid));
        Event event = eventEntity.toEvent();
        return new EventDto(event);
    }

    @Transactional
    public EventDto createEvent(EventDto eventDto, String authorization) {
        UUID userUuid = getUserUuidFromToken(authorization);
        OrganizationEntity organizationEntity = organizationPersistence.findByUserUuid(userUuid);
        Event event = new Event(eventDto);
        event.setOrganization(organizationEntity.toOrganization());
        EventEntity eventEntity = new EventEntity(event, organizationEntity);
        EventEntity createdEventEntity = eventPersistence.create(eventEntity);
        return new EventDto(createdEventEntity.toEvent());
    }

    @Transactional
    public EventDto updateEvent(String eventUuid, EventDto eventDto, String authorization) {
        UUID userUuid = getUserUuidFromToken(authorization);
        EventEntity existingEventEntity = eventPersistence.findByUuid(UUID.fromString(eventUuid));
        Event existingEvent = existingEventEntity.toEvent();
        checkUserAuthorization(existingEvent, userUuid);
        Event event = new Event(eventDto, UUID.fromString(eventUuid));
        event.setOrganization(existingEvent.getOrganization());
        EventEntity updatedEventEntity = eventPersistence.update(new EventEntity(event, existingEventEntity.getOrganization()));
        return new EventDto(updatedEventEntity.toEvent());
    }

    @Transactional
    public void deleteEvent(String eventUuid, String authorization) {
        UUID userUuid = getUserUuidFromToken(authorization);
        EventEntity existingEventEntity = eventPersistence.findByUuid(UUID.fromString(eventUuid));
        Event existingEvent = existingEventEntity.toEvent();
        checkUserAuthorization(existingEvent, userUuid);
        eventPersistence.delete(UUID.fromString(eventUuid));
    }

    private void checkUserAuthorization(Event event, UUID userUuid) {
        if (!event.getOrganization().getUser().getUserUuid().equals(userUuid)) {
            throw new SecurityException("Access Denied: Event does not belong to the organization");
        }
    }

    private UUID getUserUuidFromToken(String authorization) {
        String token = jwtService.extractToken(authorization);
        return UUID.fromString(jwtService.user(token));
    }
}
