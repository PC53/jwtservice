package io.github.pc53.jwtservice;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HmacSigner {

    public static String sign(String headerEncoded, String payloadEncoded, String secret){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            return Base64Url.encode(mac.doFinal(
                    (headerEncoded + "." + payloadEncoded).getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    };
}
