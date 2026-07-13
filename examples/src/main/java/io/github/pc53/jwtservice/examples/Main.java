package io.github.pc53.jwtservice.examples;

import io.github.pc53.jwtservice.JwtService;

public class Main {
    public static void main(String[] args) {
        JwtService service = new JwtService("Pavan");
        String customerId = "1232143";
        String token = service.generateToken(customerId);
        System.out.println(token);
    }
}
