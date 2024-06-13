package es.upm.miw.trust_tie_backend.persistence;

import es.upm.miw.trust_tie_backend.exceptions.NotFoundException;
import es.upm.miw.trust_tie_backend.persistence.entities.EventEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventPersistenceIT {

    @InjectMocks
    private EventPersistence eventPersistence;

    @Mock
    private EventRepository eventRepository;

    private EventEntity eventEntity;
    private OrganizationEntity organizationEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        organizationEntity = OrganizationEntity.builder()
                .organizationUuid(UUID.randomUUID())
                .name("Test Organization")
                .phone("123456789")
                .description("Description")
                .website("https://example.com")
                .images("image1.jpg,image2.jpg")
                .build();

        eventEntity = EventEntity.builder()
                .eventUuid(UUID.randomUUID())
                .organization(organizationEntity)
                .title("Test Event")
                .description("Event Description")
                .eventDate(LocalDateTime.now())
                .eventLocation("Event Location")
                .images("event1.jpg,event2.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindAll() {
        when(eventRepository.findAll()).thenReturn(Collections.singletonList(eventEntity));

        List<EventEntity> events = eventPersistence.findAll();
        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        assertEquals(eventEntity.getTitle(), events.get(0).getTitle());
    }

    @Test
    void testFindByUuidSuccess() {
        when(eventRepository.findById(eventEntity.getEventUuid())).thenReturn(Optional.of(eventEntity));

        EventEntity foundEvent = eventPersistence.findByUuid(eventEntity.getEventUuid());
        assertNotNull(foundEvent);
        assertEquals(eventEntity.getTitle(), foundEvent.getTitle());
    }

    @Test
    void testFindByUuidNotFound() {
        when(eventRepository.findById(eventEntity.getEventUuid())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> eventPersistence.findByUuid(eventEntity.getEventUuid()));
        assertTrue(thrown.getMessage().contains("Event not found: " + eventEntity.getEventUuid()));
    }

    @Test
    void testFindByOrganizationUuid() {
        when(eventRepository.findByOrganization_OrganizationUuid(organizationEntity.getOrganizationUuid())).thenReturn(Collections.singletonList(eventEntity));

        List<EventEntity> events = eventPersistence.findByOrganizationUuid(organizationEntity.getOrganizationUuid());
        assertNotNull(events);
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        assertEquals(eventEntity.getTitle(), events.get(0).getTitle());
    }

    @Test
    void testCreateEventSuccess() {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);

        EventEntity createdEvent = eventPersistence.create(eventEntity);
        assertNotNull(createdEvent);
        assertEquals(eventEntity.getTitle(), createdEvent.getTitle());

        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testUpdateEventSuccess() {
        when(eventRepository.existsById(eventEntity.getEventUuid())).thenReturn(true);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);

        EventEntity updatedEvent = eventPersistence.update(eventEntity);
        assertNotNull(updatedEvent);
        assertEquals(eventEntity.getTitle(), updatedEvent.getTitle());

        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testUpdateEventNotFound() {
        when(eventRepository.existsById(eventEntity.getEventUuid())).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> eventPersistence.update(eventEntity));
        assertTrue(thrown.getMessage().contains("Event not found: " + eventEntity.getEventUuid()));
    }

    @Test
    void testDeleteEventSuccess() {
        when(eventRepository.existsById(eventEntity.getEventUuid())).thenReturn(true);
        doNothing().when(eventRepository).deleteById(eventEntity.getEventUuid());

        eventPersistence.delete(eventEntity.getEventUuid());

        verify(eventRepository, times(1)).deleteById(eventEntity.getEventUuid());
    }

    @Test
    void testDeleteEventNotFound() {
        when(eventRepository.existsById(eventEntity.getEventUuid())).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> eventPersistence.delete(eventEntity.getEventUuid()));
        assertTrue(thrown.getMessage().contains("Event not found: " + eventEntity.getEventUuid()));
    }
}
