# jwtservice

A small, dependency-light JWT (JSON Web Token) library for Java — HMAC-SHA256 signing, validation, and access/refresh token rotation, built from scratch on top of `javax.crypto` and Jackson (no `jjwt`/`java-jwt`).

## Install

Not yet published to Maven Central. To consume it locally:

```bash
git clone https://github.com/PC53/jwtservice.git
cd jwtservice
./gradlew :lib:publishToMavenLocal
```

Then in your project:

```kotlin
dependencies {
    implementation("io.github.pc53:lib:0.1.0-SNAPSHOT")
}
```

(Or add this repo as a JitPack dependency once pushed to GitHub — see [jitpack.io](https://jitpack.io).)

## Usage

```java
import io.github.pc53.jwtservice.JwtService;
import io.github.pc53.jwtservice.TokenPair;
import io.github.pc53.jwtservice.exception.InvalidTokenException;

import java.util.Map;

// Reads the signing secret from the JWT_SECRET env var.
JwtService jwt = new JwtService("my-app-issuer");

// Issue an access + refresh token pair
TokenPair tokens = jwt.login("user-123");
System.out.println(tokens.accessToken());
System.out.println(tokens.refreshToken());

// Validate a token and read its claims
try {
    Map<String, Object> claims = jwt.validateToken(tokens.accessToken());
    String subject = (String) claims.get("sub");
} catch (InvalidTokenException e) {
    // signature invalid, malformed, or expired
}

// Exchange a refresh token for a new pair (rotates the old refresh token's jti)
TokenPair refreshed = jwt.refreshToken(tokens.refreshToken());
```

## Build & Test

```bash
./gradlew build              # compile + test everything
./gradlew :examples:run      # run the usage example in examples/
```

See [`CLAUDE.md`](CLAUDE.md) for module layout and architecture, and [`PROGRESS.md`](PROGRESS.md) for the build log and known open items (e.g. `nbf` claim, roles, blocklist eviction).

## Status

Early-stage / learning project — API may change without notice. Not yet recommended for production use.

## License

[MIT](LICENSE)
