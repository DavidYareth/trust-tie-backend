package es.upm.miw.trust_tie_backend.model.dtos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TokenDtoTest {

    @Test
    void testAllArgsConstructor() {
        TokenDto tokenDto = new TokenDto("sampleToken");

        assertNotNull(tokenDto);
        assertEquals("sampleToken", tokenDto.getToken());
    }

    @Test
    void testNoArgsConstructor() {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken("noargsToken");

        assertNotNull(tokenDto);
        assertEquals("noargsToken", tokenDto.getToken());
    }
}
