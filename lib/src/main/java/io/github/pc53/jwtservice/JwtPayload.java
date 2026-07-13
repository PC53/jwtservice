package io.github.pc53.jwtservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class JwtPayload {

    private String sub;
    private String iss;
    private long iat;
    private long exp;
    private String jti;

    public JwtPayload subject(String sub){
        this.sub = sub;
        return this;
    }

    public JwtPayload issuer(String iss){
        this.iss = iss;
        return this;
    }

    public JwtPayload issuedAt(long iat){
        this.iat = iat;
        return this;
    }

    public JwtPayload expiry(long exp){
        this.exp = exp;
        return this;
    }

    public JwtPayload jwtId(String jti){
        this.jti = jti;
        return this;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public String encode(){
        Map<String,Object> claims = new LinkedHashMap<>();
        if (sub != null) claims.put("sub", sub);
        if (iat > 0) claims.put("iat", iat);
        if (exp > 0) claims.put("exp", exp); // TODO : exp should be in future, maybe preset with builder
        if (jti != null) claims.put("jti", jti);
        try {
            return Base64Url.encode(MAPPER.writeValueAsBytes(claims));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    };

}
