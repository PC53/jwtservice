package io.github.pc53.jwtservice.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause){
        super(message, cause);
    }
}
