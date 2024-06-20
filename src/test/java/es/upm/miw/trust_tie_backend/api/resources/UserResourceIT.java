package es.upm.miw.trust_tie_backend.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upm.miw.trust_tie_backend.model.dtos.*;
import es.upm.miw.trust_tie_backend.services.UserService;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private LoginDto loginDto;
    private RegisterAdopterDto registerAdopterDto;
    private RegisterOrganizationDto registerOrganizationDto;

    @BeforeEach
    void beforeEach() {
        loginDto = LoginDto.builder()
                .email("test@test.com")
                .password("Password123&")
                .build();

        registerAdopterDto = RegisterAdopterDto.builder()
                .email("adopter@test.com")
                .password("Password123&")
                .firstName("First")
                .lastName("Last")
                .phone("+1234567890")
                .build();

        registerOrganizationDto = RegisterOrganizationDto.builder()
                .email("organization@test.com")
                .password("Password123&")
                .name("Organization Name")
                .phone("+1234567890")
                .build();
    }

    @Test
    void testLogin() throws Exception {
        Mockito.when(userService.login(Mockito.any())).thenReturn(new TokenDto("token"));

        mockMvc.perform(post(UserResource.USER + UserResource.LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("token")));
    }

    @Test
    void testRegisterAdopter() throws Exception {
        Mockito.when(userService.registerAdopter(Mockito.any())).thenReturn(new TokenDto("token"));

        mockMvc.perform(post(UserResource.USER + UserResource.REGISTER + UserResource.ADOPTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerAdopterDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("token")));
    }

    @Test
    void testRegisterOrganization() throws Exception {
        Mockito.when(userService.registerOrganization(Mockito.any())).thenReturn(new TokenDto("token"));

        mockMvc.perform(post(UserResource.USER + UserResource.REGISTER + UserResource.ORGANIZATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerOrganizationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("token")));
    }

    @Test
    void testGetAdopterProfile() throws Exception {
        AdopterDto adopterDto = AdopterDto.builder()
                .adopterUuid("adopter-uuid")
                .email("adopter@test.com")
                .firstName("First")
                .lastName("Last")
                .phone("+1234567890")
                .biography("Biography")
                .images("images")
                .build();

        Mockito.when(userService.getAdopterProfile(Mockito.anyString())).thenReturn(adopterDto);

        mockMvc.perform(get(UserResource.USER + UserResource.PROFILE + UserResource.ADOPTER)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(adopterDto)));
    }

    @Test
    void testGetOrganizationProfile() throws Exception {
        OrganizationDto organizationDto = OrganizationDto.builder()
                .organizationUuid("organization-uuid")
                .email("organization@test.com")
                .name("Organization Name")
                .phone("+1234567890")
                .description("Description")
                .website("http://example.com")
                .images("images")
                .build();

        Mockito.when(userService.getOrganizationProfile(Mockito.anyString())).thenReturn(organizationDto);

        mockMvc.perform(get(UserResource.USER + UserResource.PROFILE + UserResource.ORGANIZATION)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(organizationDto)));
    }
}
