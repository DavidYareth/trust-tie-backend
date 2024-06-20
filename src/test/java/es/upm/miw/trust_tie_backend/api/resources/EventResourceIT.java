package es.upm.miw.trust_tie_backend.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.upm.miw.trust_tie_backend.model.dtos.EventDto;
import es.upm.miw.trust_tie_backend.services.EventService;
import es.upm.miw.trust_tie_backend.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private JwtService jwtService;

    private EventDto eventDto;
    private String validToken;

    @BeforeEach
    void beforeEach() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        eventDto = EventDto.builder()
                .eventUuid(UUID.randomUUID().toString())
                .organizationUuid(UUID.randomUUID().toString())
                .title("Event Title")
                .description("Event Description")
                .eventDate(LocalDateTime.now())
                .eventLocation("Event Location")
                .images("images")
                .build();

        String userUuid = UUID.randomUUID().toString();
        validToken = jwtService.createToken(userUuid, "ORGANIZATION");

        Mockito.when(eventService.getEvent(Mockito.anyString())).thenReturn(eventDto);
        Mockito.when(eventService.getAllEvents()).thenReturn(Collections.singletonList(eventDto));
        Mockito.when(eventService.getEventsByOrganization(Mockito.any(UUID.class))).thenReturn(Collections.singletonList(eventDto));
        Mockito.when(eventService.getMyEvents(Mockito.anyString())).thenReturn(Collections.singletonList(eventDto));
        Mockito.when(eventService.createEvent(Mockito.any(EventDto.class), Mockito.anyString())).thenReturn(eventDto);
        Mockito.when(eventService.updateEvent(Mockito.anyString(), Mockito.any(EventDto.class), Mockito.anyString())).thenReturn(eventDto);
        Mockito.doNothing().when(eventService).deleteEvent(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testGetAllEvents() throws Exception {
        mockMvc.perform(get(EventResource.EVENTS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventUuid", is(eventDto.getEventUuid())))
                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())))
                .andExpect(jsonPath("$[0].description", is(eventDto.getDescription())))
                .andExpect(jsonPath("$[0].eventDate", containsString(eventDto.getEventDate().toLocalDate().toString())))
                .andExpect(jsonPath("$[0].eventLocation", is(eventDto.getEventLocation())))
                .andExpect(jsonPath("$[0].images", is(eventDto.getImages())));
    }

    @Test
    void testGetEvent() throws Exception {
        String eventUuid = UUID.randomUUID().toString();

        mockMvc.perform(get(EventResource.EVENTS + EventResource.EVENT_UUID, eventUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventUuid", is(eventDto.getEventUuid())))
                .andExpect(jsonPath("$.title", is(eventDto.getTitle())))
                .andExpect(jsonPath("$.description", is(eventDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", containsString(eventDto.getEventDate().toLocalDate().toString())))
                .andExpect(jsonPath("$.eventLocation", is(eventDto.getEventLocation())))
                .andExpect(jsonPath("$.images", is(eventDto.getImages())));
    }

    @Test
    void testGetEventsByOrganization() throws Exception {
        String organizationUuid = UUID.randomUUID().toString();

        mockMvc.perform(get(EventResource.EVENTS + EventResource.ORGANIZATION_UUID, organizationUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventUuid", is(eventDto.getEventUuid())))
                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())))
                .andExpect(jsonPath("$[0].description", is(eventDto.getDescription())))
                .andExpect(jsonPath("$[0].eventDate", containsString(eventDto.getEventDate().toLocalDate().toString())))
                .andExpect(jsonPath("$[0].eventLocation", is(eventDto.getEventLocation())))
                .andExpect(jsonPath("$[0].images", is(eventDto.getImages())));
    }

    @Test
    void testGetMyEvents() throws Exception {
        mockMvc.perform(get(EventResource.EVENTS + EventResource.MY_EVENTS)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventUuid", is(eventDto.getEventUuid())))
                .andExpect(jsonPath("$[0].title", is(eventDto.getTitle())))
                .andExpect(jsonPath("$[0].description", is(eventDto.getDescription())))
                .andExpect(jsonPath("$[0].eventDate", containsString(eventDto.getEventDate().toLocalDate().toString())))
                .andExpect(jsonPath("$[0].eventLocation", is(eventDto.getEventLocation())))
                .andExpect(jsonPath("$[0].images", is(eventDto.getImages())));
    }

    @Test
    void testCreateEvent() throws Exception {
        mockMvc.perform(post(EventResource.EVENTS)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(eventDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventUuid", is(eventDto.getEventUuid())))
                .andExpect(jsonPath("$.title", is(eventDto.getTitle())))
                .andExpect(jsonPath("$.description", is(eventDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", containsString(eventDto.getEventDate().toLocalDate().toString())))
                .andExpect(jsonPath("$.eventLocation", is(eventDto.getEventLocation())))
                .andExpect(jsonPath("$.images", is(eventDto.getImages())));
    }

    @Test
    void testUpdateEvent() throws Exception {
        String eventUuid = UUID.randomUUID().toString();

        mockMvc.perform(put(EventResource.EVENTS + EventResource.EVENT_UUID, eventUuid)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(eventDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventUuid", is(eventDto.getEventUuid())))
                .andExpect(jsonPath("$.title", is(eventDto.getTitle())))
                .andExpect(jsonPath("$.description", is(eventDto.getDescription())))
                .andExpect(jsonPath("$.eventDate", containsString(eventDto.getEventDate().toLocalDate().toString())))
                .andExpect(jsonPath("$.eventLocation", is(eventDto.getEventLocation())))
                .andExpect(jsonPath("$.images", is(eventDto.getImages())));
    }

    @Test
    void testDeleteEvent() throws Exception {
        String eventUuid = UUID.randomUUID().toString();

        mockMvc.perform(delete(EventResource.EVENTS + EventResource.EVENT_UUID, eventUuid)
                        .header("Authorization", validToken))
                .andExpect(status().isOk());
    }
}
