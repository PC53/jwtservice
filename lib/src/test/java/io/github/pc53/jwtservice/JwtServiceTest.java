package io.github.pc53.jwtservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtServiceTest {

    private static final String TEST_SECRET = "test-secret";

    @Test
    void expiredTokenShouldThrow(){
        String expiredToken = new JwtBuilder(TEST_SECRET)
                .subject("user-1")
                .issuedAt(System.currentTimeMillis() / 1000 - 3600)
                .expiry(System.currentTimeMillis() / 1000 - 1)
                .build();

        JwtValidator validator = new JwtValidator(TEST_SECRET);

        assertThrows(IllegalArgumentException.class, () -> validator.getDecodedPayload(expiredToken));
    }
}
