package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.persistence.entities.EventEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.EventRepository;
import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventPersistence {

    private final EventRepository eventRepository;

    public List<EventEntity> findAll() {
        return eventRepository.findAll();
    }

    public EventEntity findByUuid(UUID eventUuid) {
        return eventRepository.findById(eventUuid)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventUuid));
    }

    public List<EventEntity> findByOrganizationUuid(UUID organizationUuid) {
        return eventRepository.findByOrganization_OrganizationUuid(organizationUuid);
    }

    public EventEntity create(EventEntity eventEntity) {
        return eventRepository.save(eventEntity);
    }

    public EventEntity update(EventEntity eventEntity) {
        assertEventExists(eventEntity.getEventUuid());
        return eventRepository.save(eventEntity);
    }

    public void delete(UUID eventUuid) {
        assertEventExists(eventUuid);
        eventRepository.deleteById(eventUuid);
    }

    private void assertEventExists(UUID eventUuid) {
        if (!eventRepository.existsById(eventUuid)) {
            throw new NotFoundException("Event not found: " + eventUuid);
        }
    }
}
