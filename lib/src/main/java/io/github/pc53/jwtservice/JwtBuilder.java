package io.github.pc53.jwtservice;

public class JwtBuilder {
    private final JwtPayload jwtPayload = new JwtPayload();
    private final JwtHeader jwtHeader = new JwtHeader().algorithm("HS256").type("JWT");
    private final String secret;

    public JwtBuilder(String secret){
        this.secret = secret;
    }

    public JwtBuilder subject(String sub){
        this.jwtPayload.subject(sub);
        return this;
    }

    public JwtBuilder issuer(String iss){
        this.jwtPayload.issuer(iss);
        return this;
    }

    public JwtBuilder issuedAt(long iat){
        this.jwtPayload.issuedAt(iat);
        return this;
    }

    public JwtBuilder expiry(long exp){
        this.jwtPayload.expiry(exp);
        return this;
    }

    public JwtBuilder jwtId(String jti){
        this.jwtPayload.jwtId(jti);
        return this;
    }

    public String build(){
        String headerEncoded = jwtHeader.encode();
        String payloadEncoded = jwtPayload.encode();
        String signature = HmacSigner.sign(headerEncoded,payloadEncoded,secret);
        return headerEncoded + "." + payloadEncoded + "." + signature;
    }
}
