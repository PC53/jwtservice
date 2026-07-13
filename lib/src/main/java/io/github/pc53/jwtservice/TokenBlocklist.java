package io.github.pc53.jwtservice;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBlocklist {
    private final Set<String> blocklist = ConcurrentHashMap.newKeySet();

    public boolean isRevoked(String jti){
        return blocklist.contains(jti);
    }

    public void revoke(String jti){
        blocklist.add(jti);
    }
}
