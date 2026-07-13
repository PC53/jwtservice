package io.github.pc53.jwtservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class JwtHeader {
    private String alg;
    private String typ;

    public JwtHeader algorithm(String alg){
        this.alg = alg;
        return this;
    }

    public JwtHeader type(String typ){
        this.typ = typ;
        return this;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public String encode(){
        Map<String,Object> headers = new LinkedHashMap<>();
        if (alg != null) headers.put("alg", alg);
        if (typ != null) headers.put("typ", typ);
        try {
            return Base64Url.encode(MAPPER.writeValueAsBytes(headers));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    };
}
