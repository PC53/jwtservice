package io.github.pc53.jwtservice;

public record TokenPair(String accessToken, String refreshToken) {
    public TokenPair {
        if (accessToken == null || accessToken.isBlank()){
            throw new IllegalArgumentException("accessToken must not be null or blank");
        }
        if (refreshToken == null || refreshToken.isBlank()){
            throw new IllegalArgumentException("refreshToken must not be null or blank");
        }
    }
}

