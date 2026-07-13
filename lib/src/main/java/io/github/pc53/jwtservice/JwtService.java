package io.github.pc53.jwtservice;

import io.github.pc53.jwtservice.exception.InvalidTokenException;

import java.util.Map;
import java.util.UUID;

public class JwtService {

    private final String secret;
    private final String issuer;

    private final JwtValidator validator;
    private final TokenBlocklist tokenBlocklist;

    private static final long ACCESS_TOKEN_EXPIRY = 60*60;
    private static final long REFRESH_TOKEN_EXPIRY = 10 * 24 * 60 * 60;

    public JwtService(String issuer) {
        this.issuer = issuer;
        this.secret = System.getenv("JWT_SECRET");

        this.validator = new JwtValidator(this.secret);
        this.tokenBlocklist = new TokenBlocklist();

        if (this.secret == null) throw new IllegalStateException("JWT_SECRET env var not found");
    }

    public TokenPair login(String sub){
        String accessToken = new JwtBuilder(this.secret)
                .subject(sub)
                .issuer(this.issuer)
                .issuedAt(System.currentTimeMillis() / 1000)
                .expiry(System.currentTimeMillis()/1000 + ACCESS_TOKEN_EXPIRY)
                .build();

        String refreshToken = new JwtBuilder(this.secret)
                .subject(sub)
                .issuedAt(System.currentTimeMillis() / 1000)
                .jwtId(UUID.randomUUID().toString())
                .expiry(System.currentTimeMillis()/1000 + REFRESH_TOKEN_EXPIRY)
                .build();

        return new TokenPair(accessToken,refreshToken);
    }

    public String generateToken(String sub){
        return new JwtBuilder(this.secret)
                .subject(sub)
                .issuer(this.issuer)
                .issuedAt(System.currentTimeMillis() / 1000)
                .expiry(System.currentTimeMillis()/1000 + ACCESS_TOKEN_EXPIRY)
                .build();
    }

    public Map<String, Object> validateToken(String token){
        try{
            return this.validator.getDecodedPayload(token);
        } catch (IllegalArgumentException e){
            throw new InvalidTokenException("Token validation failed: " + e.getMessage(), e);
        }
    }

    public TokenPair refreshToken(String refreshToken){
        Map<String, Object> payload = this.validator.getDecodedPayload(refreshToken);
        String jti = payload.get("jti").toString();
        if (this.tokenBlocklist.isRevoked(jti)) throw new InvalidTokenException("Refresh token revoked");

        this.tokenBlocklist.revoke(jti);
        String sub = payload.get("sub").toString();
        return this.login(sub);
    }
}
