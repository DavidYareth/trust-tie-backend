package es.upm.miw.trust_tie_backend.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.upm.miw.trust_tie_backend.model.dtos.AnimalDto;
import es.upm.miw.trust_tie_backend.services.AnimalService;
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

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnimalResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @Autowired
    private JwtService jwtService;

    private AnimalDto animalDto;
    private String validToken;

    @BeforeEach
    void beforeEach() {
        animalDto = AnimalDto.builder()
                .animalUuid(UUID.randomUUID().toString())
                .organizationUuid(UUID.randomUUID().toString())
                .name("Animal Name")
                .type("Dog")
                .breed("Breed")
                .age(2)
                .size("Medium")
                .characteristics("Friendly")
                .build();

        String userUuid = UUID.randomUUID().toString();
        validToken = jwtService.createToken(userUuid, "ORGANIZATION");

        Mockito.when(animalService.getAnimal(Mockito.anyString())).thenReturn(animalDto);
        Mockito.when(animalService.getAllAnimals()).thenReturn(Collections.singletonList(animalDto));
        Mockito.when(animalService.getAnimalsByOrganization(Mockito.any(UUID.class))).thenReturn(Collections.singletonList(animalDto));
        Mockito.when(animalService.getMyAnimals(Mockito.anyString())).thenReturn(Collections.singletonList(animalDto));
        Mockito.when(animalService.createAnimal(Mockito.any(AnimalDto.class), Mockito.anyString())).thenReturn(animalDto);
        Mockito.when(animalService.updateAnimal(Mockito.anyString(), Mockito.any(AnimalDto.class), Mockito.anyString())).thenReturn(animalDto);
        Mockito.doNothing().when(animalService).deleteAnimal(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testGetAllAnimals() throws Exception {
        mockMvc.perform(get(AnimalResource.ANIMALS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].animalUuid", is(animalDto.getAnimalUuid())))
                .andExpect(jsonPath("$[0].name", is(animalDto.getName())))
                .andExpect(jsonPath("$[0].type", is(animalDto.getType())))
                .andExpect(jsonPath("$[0].breed", is(animalDto.getBreed())))
                .andExpect(jsonPath("$[0].age", is(animalDto.getAge())))
                .andExpect(jsonPath("$[0].size", is(animalDto.getSize())))
                .andExpect(jsonPath("$[0].characteristics", is(animalDto.getCharacteristics())));
    }

    @Test
    void testGetAnimal() throws Exception {
        String animalUuid = UUID.randomUUID().toString();

        mockMvc.perform(get(AnimalResource.ANIMALS + AnimalResource.ANIMAL_UUID, animalUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animalUuid", is(animalDto.getAnimalUuid())))
                .andExpect(jsonPath("$.name", is(animalDto.getName())))
                .andExpect(jsonPath("$.type", is(animalDto.getType())))
                .andExpect(jsonPath("$.breed", is(animalDto.getBreed())))
                .andExpect(jsonPath("$.age", is(animalDto.getAge())))
                .andExpect(jsonPath("$.size", is(animalDto.getSize())))
                .andExpect(jsonPath("$.characteristics", is(animalDto.getCharacteristics())));
    }

    @Test
    void testGetAnimalsByOrganization() throws Exception {
        String organizationUuid = UUID.randomUUID().toString();

        mockMvc.perform(get(AnimalResource.ANIMALS + AnimalResource.ORGANIZATION_UUID, organizationUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].animalUuid", is(animalDto.getAnimalUuid())))
                .andExpect(jsonPath("$[0].name", is(animalDto.getName())))
                .andExpect(jsonPath("$[0].type", is(animalDto.getType())))
                .andExpect(jsonPath("$[0].breed", is(animalDto.getBreed())))
                .andExpect(jsonPath("$[0].age", is(animalDto.getAge())))
                .andExpect(jsonPath("$[0].size", is(animalDto.getSize())))
                .andExpect(jsonPath("$[0].characteristics", is(animalDto.getCharacteristics())));
    }

    @Test
    void testGetMyAnimals() throws Exception {
        mockMvc.perform(get(AnimalResource.ANIMALS + AnimalResource.MY_ANIMALS)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].animalUuid", is(animalDto.getAnimalUuid())))
                .andExpect(jsonPath("$[0].name", is(animalDto.getName())))
                .andExpect(jsonPath("$[0].type", is(animalDto.getType())))
                .andExpect(jsonPath("$[0].breed", is(animalDto.getBreed())))
                .andExpect(jsonPath("$[0].age", is(animalDto.getAge())))
                .andExpect(jsonPath("$[0].size", is(animalDto.getSize())))
                .andExpect(jsonPath("$[0].characteristics", is(animalDto.getCharacteristics())));
    }

    @Test
    void testCreateAnimal() throws Exception {
        mockMvc.perform(post(AnimalResource.ANIMALS)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(animalDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animalUuid", is(animalDto.getAnimalUuid())))
                .andExpect(jsonPath("$.name", is(animalDto.getName())))
                .andExpect(jsonPath("$.type", is(animalDto.getType())))
                .andExpect(jsonPath("$.breed", is(animalDto.getBreed())))
                .andExpect(jsonPath("$.age", is(animalDto.getAge())))
                .andExpect(jsonPath("$.size", is(animalDto.getSize())))
                .andExpect(jsonPath("$.characteristics", is(animalDto.getCharacteristics())));
    }

    @Test
    void testUpdateAnimal() throws Exception {
        String animalUuid = UUID.randomUUID().toString();

        mockMvc.perform(put(AnimalResource.ANIMALS + AnimalResource.ANIMAL_UUID, animalUuid)
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(animalDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animalUuid", is(animalDto.getAnimalUuid())))
                .andExpect(jsonPath("$.name", is(animalDto.getName())))
                .andExpect(jsonPath("$.type", is(animalDto.getType())))
                .andExpect(jsonPath("$.breed", is(animalDto.getBreed())))
                .andExpect(jsonPath("$.age", is(animalDto.getAge())))
                .andExpect(jsonPath("$.size", is(animalDto.getSize())))
                .andExpect(jsonPath("$.characteristics", is(animalDto.getCharacteristics())));
    }

    @Test
    void testDeleteAnimal() throws Exception {
        String animalUuid = UUID.randomUUID().toString();

        mockMvc.perform(delete(AnimalResource.ANIMALS + AnimalResource.ANIMAL_UUID, animalUuid)
                        .header("Authorization", validToken))
                .andExpect(status().isOk());
    }
}
