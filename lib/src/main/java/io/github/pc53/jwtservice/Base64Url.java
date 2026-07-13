package io.github.pc53.jwtservice;

import java.util.Base64;

public class Base64Url {

    public static String encode(byte[] data){
        return Base64.getUrlEncoder().encodeToString(data).replace("=", "");
    }

    public static byte[] decode(String encoded){
        int remainder = encoded.length() % 4;
        if (remainder > 0) {
            encoded += "=".repeat(4 - remainder);
        }
        return Base64.getUrlDecoder().decode(encoded);
    }

}
