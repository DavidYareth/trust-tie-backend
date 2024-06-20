package es.upm.miw.trust_tie_backend.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upm.miw.trust_tie_backend.model.dtos.OrganizationDto;
import es.upm.miw.trust_tie_backend.services.OrganizationService;
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
public class OrganizationResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService organizationService;

    @Autowired
    private JwtService jwtService;

    private OrganizationDto organizationDto;
    private String validToken;

    @BeforeEach
    void beforeEach() {
        organizationDto = OrganizationDto.builder()
                .organizationUuid(UUID.randomUUID().toString())
                .name("Organization Name")
                .phone("+1234567890")
                .description("Description")
                .website("http://example.com")
                .images("images")
                .build();

        String userUuid = UUID.randomUUID().toString();
        validToken = jwtService.createToken(userUuid, "ORGANIZATION");

        Mockito.when(organizationService.getOrganization(Mockito.anyString())).thenReturn(organizationDto);
        Mockito.when(organizationService.updateOrganization(Mockito.anyString(), Mockito.any(), Mockito.anyString()))
                .thenReturn(organizationDto);
        Mockito.doNothing().when(organizationService).deleteOrganization(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testGetOrganization() throws Exception {
        String organizationUuid = UUID.randomUUID().toString();

        mockMvc.perform(get(OrganizationResource.ORGANIZATION + OrganizationResource.ORGANIZATION_UUID, organizationUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationUuid", is(organizationDto.getOrganizationUuid())))
                .andExpect(jsonPath("$.name", is(organizationDto.getName())))
                .andExpect(jsonPath("$.phone", is(organizationDto.getPhone())))
                .andExpect(jsonPath("$.description", is(organizationDto.getDescription())))
                .andExpect(jsonPath("$.website", is(organizationDto.getWebsite())))
                .andExpect(jsonPath("$.images", is(organizationDto.getImages())));
    }

    @Test
    void testUpdateOrganization() throws Exception {
        String organizationUuid = UUID.randomUUID().toString();

        mockMvc.perform(put(OrganizationResource.ORGANIZATION + OrganizationResource.ORGANIZATION_UUID, organizationUuid)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(organizationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.organizationUuid", is(organizationDto.getOrganizationUuid())))
                .andExpect(jsonPath("$.name", is(organizationDto.getName())))
                .andExpect(jsonPath("$.phone", is(organizationDto.getPhone())))
                .andExpect(jsonPath("$.description", is(organizationDto.getDescription())))
                .andExpect(jsonPath("$.website", is(organizationDto.getWebsite())))
                .andExpect(jsonPath("$.images", is(organizationDto.getImages())));
    }

    @Test
    void testDeleteOrganization() throws Exception {
        String organizationUuid = UUID.randomUUID().toString();

        mockMvc.perform(delete(OrganizationResource.ORGANIZATION + OrganizationResource.ORGANIZATION_UUID, organizationUuid)
                        .header("Authorization", validToken))
                .andExpect(status().isOk());
    }
}
