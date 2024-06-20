package es.upm.miw.trust_tie_backend.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upm.miw.trust_tie_backend.model.dtos.AdopterDto;
import es.upm.miw.trust_tie_backend.services.AdopterService;
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

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdopterResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdopterService adopterService;

    @Autowired
    private JwtService jwtService;

    private AdopterDto adopterDto;
    private String validToken;

    @BeforeEach
    void beforeEach() {
        adopterDto = AdopterDto.builder()
                .adopterUuid(UUID.randomUUID().toString())
                .firstName("First")
                .lastName("Last")
                .phone("+1234567890")
                .biography("Biography")
                .images("images")
                .build();

        String userUuid = UUID.randomUUID().toString();
        validToken = jwtService.createToken(userUuid, "ADOPTER");

        Mockito.when(adopterService.getAdopter(Mockito.anyString())).thenReturn(adopterDto);
        Mockito.when(adopterService.updateAdopter(Mockito.anyString(), Mockito.any(), Mockito.anyString()))
                .thenReturn(adopterDto);
        Mockito.doNothing().when(adopterService).deleteAdopter(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testGetAdopter() throws Exception {
        String adopterUuid = UUID.randomUUID().toString();

        mockMvc.perform(get(AdopterResource.ADOPTER + AdopterResource.ADOPTER_UUID, adopterUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adopterUuid", is(adopterDto.getAdopterUuid())))
                .andExpect(jsonPath("$.firstName", is(adopterDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(adopterDto.getLastName())))
                .andExpect(jsonPath("$.phone", is(adopterDto.getPhone())))
                .andExpect(jsonPath("$.biography", is(adopterDto.getBiography())))
                .andExpect(jsonPath("$.images", is(adopterDto.getImages())));
    }

    @Test
    void testUpdateAdopter() throws Exception {
        String adopterUuid = UUID.randomUUID().toString();

        mockMvc.perform(put(AdopterResource.ADOPTER + AdopterResource.ADOPTER_UUID, adopterUuid)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adopterDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adopterUuid", is(adopterDto.getAdopterUuid())))
                .andExpect(jsonPath("$.firstName", is(adopterDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(adopterDto.getLastName())))
                .andExpect(jsonPath("$.phone", is(adopterDto.getPhone())))
                .andExpect(jsonPath("$.biography", is(adopterDto.getBiography())))
                .andExpect(jsonPath("$.images", is(adopterDto.getImages())));
    }

    @Test
    void testDeleteAdopter() throws Exception {
        String adopterUuid = UUID.randomUUID().toString();

        mockMvc.perform(delete(AdopterResource.ADOPTER + AdopterResource.ADOPTER_UUID, adopterUuid)
                        .header("Authorization", validToken))
                .andExpect(status().isOk());
    }
}
