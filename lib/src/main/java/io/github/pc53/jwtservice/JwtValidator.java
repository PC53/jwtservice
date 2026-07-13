package io.github.pc53.jwtservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

public class JwtValidator {
    private final String secret;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JwtValidator(String secret){
        this.secret = secret;
    }

    public Map<String, Object> getDecodedPayload(String token){
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid token format");

        if (!this.isSignatureValid(parts)) throw new IllegalArgumentException("Invalid token signature");

        Map<String, Object> decodedPayload = this.decode(parts[1]);
        if (System.currentTimeMillis() / 1000 > ((Number) decodedPayload.get("exp")).longValue()) {
            throw new IllegalArgumentException("Token has expired");
        }
        return decodedPayload;
    }

    private boolean isSignatureValid(String[] parts){
        String newSignature = HmacSigner.sign(parts[0],parts[1], this.secret);
        // constant time comparison to prevent timing-attacks
        return MessageDigest.isEqual(
                newSignature.getBytes(StandardCharsets.UTF_8),
                parts[2].getBytes(StandardCharsets.UTF_8)
        );
    }

    private Map<String, Object> decode(String part){
        try {
            return MAPPER.readValue(Base64Url.decode(part), new TypeReference<Map<String,Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
