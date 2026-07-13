# JWT Library — Tutorial Progress

## Final Architecture

```
JwtService          ← public API (only class users need)
  ├─ generateToken(...) → String
  ├─ validateToken(...) → Claims
  └─ refreshToken(...)  → TokenPair

Internal (not part of public API):
  JwtBuilder        ← builds and signs token string
  JwtPayload        ← holds and encodes claims
  JwtHeader         ← encodes header
  HmacSigner        ← HMAC-SHA256 signing
  Base64Url         ← encoding utility
  TokenPair         ← access + refresh token pair (Stage 8)
```

Secret is passed once to `JwtService` at construction (loaded from env var by the caller) — never exposed to users of the library.

---

## Stage 1 — JWT Anatomy ✅
- [x] Decode a raw JWT manually (split on `.`, Base64URL-decode header and payload)

**Concepts:** Three-part structure `header.payload.signature`, what each part contains, why the payload is readable but not forgeable.

---

## Stage 2 — Base64URL Encoding ✅
- [x] Understand the difference between Base64 and Base64URL
- [x] Implement a `Base64Url` utility (encode/decode)

**Concepts:** URL-safe alphabet (`-` and `_` instead of `+` and `/`), no padding (`=` stripped).

---

## Stage 3 — Header & Payload ✅
- [x] Build a `JwtHeader` (alg, typ)
- [x] Build a `JwtPayload` / claims map (sub, iat, exp, custom claims)
- [x] Serialize both to JSON using Jackson and Base64URL-encode them

**Concepts:** Standard registered claims (`sub`, `iss`, `aud`, `iat`, `exp`, `jti`), custom claims, Jackson `ObjectMapper` for serialization.

---

## Stage 4 — HMAC-SHA256 Signing ✅
- [x] Implement signing using `javax.crypto.Mac` with `HmacSHA256`
- [x] Understand what input is signed (`base64url(header) + "." + base64url(payload)`)

**Concepts:** HMAC (Hash-based Message Authentication Code), why we sign the encoded form not the raw JSON, secret key management basics.

---

## Stage 5 — Token Generation ✅
- [x] Implement `JwtBuilder` — fluent internal API to set claims and produce a signed token string
- [x] Wire together: header → payload → sign → concatenate
- [x] Wrap behind `JwtService` — secret held by service, not exposed to callers

**Concepts:** Builder pattern, `JwtService` as the single public entry point, secrets via environment variables not source code.

---

## Stage 6 — Token Validation & Parsing ✅
- [x] Implement `JwtService.validateToken(token)` — returns `Map<String, Object>` of claims
- [x] Re-compute signature and compare using constant-time comparison (`MessageDigest.isEqual`)
- [x] Parse claims from payload using Jackson
- [x] Throw `InvalidTokenException` (custom) — `JwtValidator` throws `IllegalArgumentException` internally, `JwtService` translates it via exception translation pattern

**Concepts:** Timing-safe comparison (`MessageDigest.isEqual`), exception translation, global exception handlers in frameworks like Spring Boot log full stack traces via `log.error("msg", e)`.

---

## Stage 7 — Claims: Expiry & Roles
- [ ] Enforce `exp` claim during validation (reject expired tokens)
- [ ] Enforce `nbf` (not-before) claim
- [ ] Embed and extract roles/permissions as a custom claim
- [ ] Implement `JwtService.hasRole(token, role)` helper

**Concepts:** Standard time-based claims, scopes vs roles, claim extraction API.

---

## Stage 8 — Refresh Token Flow
- [x] Design `TokenPair` (access token + refresh token)
- [x] Access token: short-lived, carries identity (roles deferred to Stage 7)
- [x] Refresh token: long-lived, carries only `sub` + `jti`
- [x] Implement `JwtService.refreshToken(refreshToken)` → new `TokenPair`
- [x] Implement refresh token revocation (in-memory blocklist using `jti`)
- [ ] Fix unbounded growth of `TokenBlocklist` — revoked `jti`s are never evicted, even after the underlying token would've naturally expired. Store `(jti, expiry)` pairs and prune expired entries (or TTL-evict).

**Concepts:** Why two tokens, `jti` (JWT ID) for revocation, stateless vs stateful trade-offs.

---

## Status

| Stage | Topic | Status |
|-------|-------|--------|
| 1 | JWT Anatomy | Complete |
| 2 | Base64URL Encoding | Complete |
| 3 | Header & Payload | Complete |
| 4 | HMAC-SHA256 Signing | Complete |
| 5 | Token Generation | Complete |
| 6 | Token Validation & Parsing | Complete |
| 7 | Claims: Expiry & Roles | In Progress |
| 8 | Refresh Token Flow | In Progress (blocklist eviction outstanding) |
