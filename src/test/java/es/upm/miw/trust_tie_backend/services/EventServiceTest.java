package es.upm.miw.trust_tie_backend.services;

import es.upm.miw.trust_tie_backend.model.Role;
import es.upm.miw.trust_tie_backend.model.dtos.EventDto;
import es.upm.miw.trust_tie_backend.persistence.EventPersistence;
import es.upm.miw.trust_tie_backend.persistence.OrganizationPersistence;
import es.upm.miw.trust_tie_backend.persistence.entities.EventEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.OrganizationEntity;
import es.upm.miw.trust_tie_backend.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password1";
    private static final String TEST_TOKEN = "token";
    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final String TEST_UUID_STRING = TEST_UUID.toString();

    @Mock
    private EventPersistence eventPersistence;

    @Mock
    private OrganizationPersistence organizationPersistence;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        EventEntity eventEntity = createEventEntity();
        when(eventPersistence.findAll()).thenReturn(Stream.of(eventEntity).collect(Collectors.toList()));

        List<EventDto> eventDtos = eventService.getAllEvents();
        assertNotNull(eventDtos);
        assertEquals(1, eventDtos.size());
        assertEquals("Charity Run", eventDtos.get(0).getTitle());
    }

    @Test
    void testGetEvent() {
        EventEntity eventEntity = createEventEntity();
        when(eventPersistence.findByUuid(any(UUID.class))).thenReturn(eventEntity);

        EventDto eventDto = eventService.getEvent(TEST_UUID_STRING);
        assertNotNull(eventDto);
        assertEquals("Charity Run", eventDto.getTitle());
    }

    @Test
    void testGetEventsByOrganization() {
        EventEntity eventEntity = createEventEntity();
        when(eventPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(Stream.of(eventEntity).collect(Collectors.toList()));

        List<EventDto> eventDtos = eventService.getEventsByOrganization(TEST_UUID);
        assertNotNull(eventDtos);
        assertEquals(1, eventDtos.size());
        assertEquals("Charity Run", eventDtos.get(0).getTitle());
    }

    @Test
    void testGetMyEvents() {
        OrganizationEntity organizationEntity = createOrganizationEntity();
        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(organizationPersistence.findByUserUuid(any(UUID.class))).thenReturn(organizationEntity);
        when(eventPersistence.findByOrganizationUuid(any(UUID.class))).thenReturn(Stream.of(createEventEntity()).collect(Collectors.toList()));

        List<EventDto> eventDtos = eventService.getMyEvents("Bearer " + TEST_TOKEN);
        assertNotNull(eventDtos);
        assertEquals(1, eventDtos.size());
        assertEquals("Charity Run", eventDtos.get(0).getTitle());
    }

    @Test
    void testCreateEvent() {
        OrganizationEntity organizationEntity = createOrganizationEntity();
        EventDto eventDto = createEventDto();
        EventEntity eventEntity = createEventEntity();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(organizationPersistence.findByUserUuid(any(UUID.class))).thenReturn(organizationEntity);
        when(eventPersistence.create(any(EventEntity.class))).thenReturn(eventEntity);

        EventDto createdEventDto = eventService.createEvent(eventDto, "Bearer " + TEST_TOKEN);
        assertNotNull(createdEventDto);
        assertEquals("Charity Run", createdEventDto.getTitle());
    }

    @Test
    void testUpdateEvent() {
        EventDto eventDto = createEventDto();
        EventEntity eventEntity = createEventEntity();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(eventPersistence.findByUuid(any(UUID.class))).thenReturn(eventEntity);
        when(eventPersistence.update(any(EventEntity.class))).thenReturn(eventEntity);

        EventDto updatedEventDto = eventService.updateEvent(TEST_UUID_STRING, eventDto, "Bearer " + TEST_TOKEN);
        assertNotNull(updatedEventDto);
        assertEquals("Charity Run", updatedEventDto.getTitle());
    }

    @Test
    void testDeleteEvent() {
        EventEntity eventEntity = createEventEntity();

        when(jwtService.extractToken(anyString())).thenReturn(TEST_TOKEN);
        when(jwtService.user(anyString())).thenReturn(TEST_UUID_STRING);
        when(eventPersistence.findByUuid(any(UUID.class))).thenReturn(eventEntity);

        doNothing().when(eventPersistence).delete(any(UUID.class));

        eventService.deleteEvent(TEST_UUID_STRING, "Bearer " + TEST_TOKEN);

        verify(eventPersistence, times(1)).delete(TEST_UUID);
    }

    private EventEntity createEventEntity() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        OrganizationEntity organizationEntity = OrganizationEntity.builder()
                .organizationUuid(TEST_UUID)
                .user(userEntity)
                .name("OrgName")
                .phone("+123456789")
                .build();

        return EventEntity.builder()
                .eventUuid(TEST_UUID)
                .organization(organizationEntity)
                .title("Charity Run")
                .description("A charity run event")
                .eventDate(LocalDateTime.now())
                .eventLocation("Central Park")
                .images("image1.jpg")
                .build();
    }

    private OrganizationEntity createOrganizationEntity() {
        UserEntity userEntity = UserEntity.builder()
                .userUuid(TEST_UUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .role(Role.ORGANIZATION)
                .build();

        return OrganizationEntity.builder()
                .organizationUuid(TEST_UUID)
                .user(userEntity)
                .name("OrgName")
                .phone("+123456789")
                .build();
    }

    private EventDto createEventDto() {
        return EventDto.builder()
                .eventUuid(TEST_UUID_STRING)
                .title("Charity Run")
                .description("A charity run event")
                .eventDate(LocalDateTime.now())
                .eventLocation("Central Park")
                .images("image1.jpg")
                .build();
    }
}
