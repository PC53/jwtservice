# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
./gradlew build                    # compile and run tests, all modules
./gradlew :examples:run            # run the usage example (application plugin)
./gradlew test                     # run all tests
./gradlew :lib:test --tests "io.github.pc53.jwtservice.SomeTest#methodName"  # run a single test
./gradlew clean                    # clean build outputs
```

## Project Setup

- **Build system**: Gradle 9.3.0 with Kotlin DSL, multi-module (`lib` + `examples`)
- **Language**: Java, group `io.github.pc53`
- **Test framework**: JUnit Jupiter 6.0.0
- **Library root package**: `io.github.pc53.jwtservice`, under `lib/src/main/java/`
- **Example entry point**: `examples/src/main/java/io/github/pc53/jwtservice/examples/Main.java`

## Module Layout

- `lib/` — the JWT library itself (`java-library` plugin). This is what gets published/consumed.
- `examples/` — a runnable usage demo (`application` plugin), depends on `:lib`. Not part of the published artifact.

## Architecture

```
JwtService          ← public API (only class users need)
  ├─ login(sub)          → TokenPair (access + refresh)
  ├─ generateToken(sub)  → String (bare access token, no refresh)
  ├─ validateToken(...)  → Map<String, Object> claims
  └─ refreshToken(...)   → TokenPair (rotates jti, revokes old refresh token)

Internal (package-private-ish, not meant for external use):
  JwtBuilder        ← builds and signs token string
  JwtPayload        ← holds and encodes claims
  JwtHeader         ← encodes header
  HmacSigner        ← HMAC-SHA256 signing
  Base64Url         ← encoding utility
  JwtValidator      ← signature verification + exp check
  TokenBlocklist    ← in-memory revoked-jti store, keyed by JwtService instance
  TokenPair         ← access + refresh token pair (record)

io.github.pc53.jwtservice.exception.InvalidTokenException — public checked-to-unchecked
  translation of internal validation failures (JwtValidator throws IllegalArgumentException
  internally; JwtService translates it at the API boundary).
```

Secret is loaded from the `JWT_SECRET` env var inside `JwtService`'s constructor — never passed in source, never exposed to callers.

## Progress / Tutorial Log

See `PROGRESS.md` for the stage-by-stage build log (this project was built incrementally to learn JWT internals). Known open items live there, not here — check it before assuming a feature (e.g. `nbf`, roles, blocklist eviction) is done.
