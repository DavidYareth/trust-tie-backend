package es.upm.miw.trust_tie_backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    private static final String BEARER = "Bearer ";
    private static final int PARTS = 3;
    private static final String UUID_CLAIM = "uuid";
    private static final String ROLE_CLAIM = "role";

    private final String secret;
    private final String issuer;
    private final int expire;

    public JwtService(@Value("${miw.jwt.secret}") String secret, @Value("${miw.jwt.issuer}") String issuer,
                      @Value("${miw.jwt.expire}") int expire) {
        this.secret = secret;
        this.issuer = issuer;
        this.expire = expire;
    }

    public String extractToken(String bearer) {
        if (bearer != null && bearer.startsWith(BEARER) && PARTS == bearer.split("\\.").length) {
            return bearer.substring(BEARER.length());
        } else {
            return "";
        }
    }

    public String createToken(String uuid, String role) {
        return BEARER + JWT.create()
                .withIssuer(this.issuer)
                .withIssuedAt(new Date())
                .withNotBefore(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + this.expire * 1000L))
                .withClaim(UUID_CLAIM, uuid)
                .withClaim(ROLE_CLAIM, role)
                .sign(Algorithm.HMAC256(this.secret));
    }

    public String user(String authorization) {
        return this.verify(authorization)
                .map(jwt -> jwt.getClaim(UUID_CLAIM).asString())
                .orElse("");
    }

    public String role(String authorization) {
        return this.verify(authorization)
                .map(jwt -> jwt.getClaim(ROLE_CLAIM).asString())
                .orElse("");
    }

    private Optional<DecodedJWT> verify(String token) {
        try {
            return Optional.of(JWT.require(Algorithm.HMAC256(this.secret))
                    .withIssuer(this.issuer).build()
                    .verify(token));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
