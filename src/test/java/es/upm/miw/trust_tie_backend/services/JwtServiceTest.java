package es.upm.miw.trust_tie_backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = JwtService.class)
class JwtServiceTest {

    private static final String TEST_UUID = "test-uuid";
    private static final String TEST_ROLE = "TEST";

    @MockBean
    private JwtService jwtService;

    @Value("${miw.jwt.secret}")
    private String secret;

    @Value("${miw.jwt.issuer}")
    private String issuer;

    @Value("${miw.jwt.expire}")
    private int expire;

    private String token;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secret, issuer, expire);
        this.token = jwtService.createToken(TEST_UUID, TEST_ROLE);
    }

    @Test
    void testCreateToken() {
        assertNotNull(this.token);
    }

    @Test
    void testExtractToken() {
        String tokenWithoutBearer = this.token.split(" ")[1];
        assertEquals(tokenWithoutBearer, jwtService.extractToken(this.token));
    }

    @Test
    void testUser() {
        String extractedToken = jwtService.extractToken(this.token);
        assertEquals(TEST_UUID, jwtService.user(extractedToken));
    }

    @Test
    void testRole() {
        String extractedToken = jwtService.extractToken(this.token);
        assertEquals("ROLE_" + TEST_ROLE, jwtService.role(extractedToken));
    }

    @Test
    void testVerifyValidToken() {
        String extractedToken = jwtService.extractToken(this.token);
        assertTrue(jwtService.verify(extractedToken).isPresent());
    }

    @Test
    void testVerifyInvalidToken() {
        assertFalse(jwtService.verify("invalid-token").isPresent());
    }
}
