---
name: volvo-api-integration
description: "Guide for integrating the Volvo Kotlin API library into a project. Use this skill when the user wants to add Volvo vehicle API support to their app, set up the Gradle dependency, configure authentication (OAuth2 or test tokens), make API calls to Volvo Connected Vehicle/Energy/Location endpoints, handle errors, or query vehicle data like location, fuel, battery, door locks, or remote commands. Also trigger when the user asks about Volvo API setup, VCC API key, Volvo ID tokens, or how to call any Volvo vehicle endpoint from Kotlin."
---

# Integrating the Volvo Kotlin API

## Setup

### 1. Add the repository and dependency

The library is published to GitHub Packages, which requires authentication even for public packages.

```kotlin
// settings.gradle.kts (or root build.gradle.kts)
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/AYastrebov/Volvo-Kotlin-API")
        credentials {
            username = providers.gradleProperty("gpr.user").orNull
                ?: System.getenv("GITHUB_ACTOR")
            password = providers.gradleProperty("gpr.token").orNull
                ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

Store credentials in `~/.gradle/gradle.properties` (never commit these):
```properties
gpr.user=your-github-username
gpr.token=ghp_your-personal-access-token
```

The GitHub token needs `read:packages` scope. Generate one at https://github.com/settings/tokens.

### 2. Add the dependency

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.github.ayastrebov.volvo:volvo-api-client:0.6.0")
}
```

### 3. Add a Ktor HTTP engine

The library needs a Ktor engine at runtime. Pick one for your platform:

```kotlin
dependencies {
    // JVM / Android
    implementation("io.ktor:ktor-client-okhttp:3.4.3")

    // iOS / macOS (in iosMain or darwinMain source set)
    implementation("io.ktor:ktor-client-darwin:3.4.3")

    // JS (Node.js)
    implementation("io.ktor:ktor-client-js:3.4.3")

    // Common multiplatform (works everywhere but less optimized)
    implementation("io.ktor:ktor-client-cio:3.4.3")
}
```

Without an engine, you get a runtime crash: `No Ktor HttpClient engine configured`.

## Authentication

You need two things from the [Volvo Developer Portal](https://developer.volvocars.com/):
- **VCC API Key** — create an application to get one
- **OAuth2 credentials** — publish your app to receive `client_id` and `client_secret`

### Production: OAuth2 with automatic token refresh

This is the recommended approach. The client automatically refreshes expired tokens.

```kotlin
val client = VolvoCars(
    VolvoCarsConfig(
        apiKey = "your-vcc-api-key",
        oauth = OAuthConfig(
            accessToken = storedAccessToken,       // from initial auth flow
            refreshToken = storedRefreshToken,     // from initial auth flow
            clientId = "your-client-id",
            clientSecret = "your-client-secret",
            onTokensRefreshed = { newAccess, newRefresh ->
                // IMPORTANT: persist both tokens immediately
                // Volvo ID invalidates the old refresh token on each use
                tokenStorage.save(newAccess, newRefresh)
            }
        )
    )
)
```

The initial `accessToken` and `refreshToken` come from completing the OAuth2 authorization code flow (redirect user to Volvo ID login, exchange code for tokens). The library handles refresh from there.

### Testing: Static token

For quick testing, generate a test token from the Developer Portal:

```kotlin
val client = VolvoCars(apiKey = "your-vcc-api-key", token = "your-test-token")
```

Test tokens expire and cannot be refreshed. Good for prototyping, not production.

### Getting the initial OAuth2 tokens (PKCE)

Volvo enforces PKCE for all new applications. The library provides a helper:

```kotlin
val pkce = Pkce.generate()

// Step 1: Build the authorization URL and redirect the user
val authUrl = "https://volvoid.eu.volvocars.com/as/authorization.oauth2" +
    "?response_type=code" +
    "&client_id=$clientId" +
    "&redirect_uri=${URLEncoder.encode(redirectUri, "UTF-8")}" +
    "&scope=${URLEncoder.encode("openid conve:vehicle_relation conve:commands", "UTF-8")}" +
    "&code_challenge=${pkce.codeChallenge}" +
    "&code_challenge_method=${pkce.codeChallengeMethod}" +
    "&state=$randomState"

// Step 2: After redirect, exchange the code for tokens
// POST to https://volvoid.eu.volvocars.com/as/token.oauth2
// with: grant_type=authorization_code&code=<code>&redirect_uri=<uri>&code_verifier=${pkce.codeVerifier}
```

## Making API calls

All API methods are suspending functions — call them from a coroutine scope.

### List vehicles

```kotlin
val vehicles = client.getVehicleList()
vehicles.data?.forEach { vehicle ->
    println("VIN: ${vehicle.vin}")
}
```

### Vehicle status

```kotlin
val vin = "YV1XZ..."

// Location (GeoJSON)
val location = client.getVehicleLocation(vin)
val coords = location.data?.geometry
println("Lat: ${coords?.latitude}, Lon: ${coords?.longitude}")

// Fuel level
val fuel = client.getFuelAmount(vin)

// Battery / charging (electric/hybrid)
val energy = client.getEnergyState(vin)

// Door & lock status
val doors = client.getDoorAndLockStatus(vin)

// Odometer
val odo = client.getOdometer(vin)

// Tyre pressure
val tyres = client.getTyreStatus(vin)

// Engine status
val engine = client.getEngineStatus(vin)
```

### Remote commands

```kotlin
// Lock / Unlock
client.invokeLock(vin)
client.invokeUnlock(vin)

// Honk & flash
client.invokeHonk(vin)
client.invokeFlash(vin)
client.invokeHonkFlash(vin)

// Engine
client.invokeEngineStart(vin, EngineStartRequest(duration = 15))
client.invokeEngineStop(vin)

// Climatization
client.invokeClimatizationStart(vin)
client.invokeClimatizationStop(vin)
```

### Distributed tracing

Include a W3C traceparent header for end-to-end observability:

```kotlin
val trace = Traceparent.generate()
val location = client.getVehicleLocation(
    vin = vin,
    requestOptions = RequestOptions(
        headers = mapOf("traceparent" to trace.value)
    )
)
println("Trace ID: ${trace.traceId}")
```

## Error handling

All API errors are typed exceptions. Catch them specifically:

```kotlin
try {
    val location = client.getVehicleLocation(vin)
} catch (e: AuthenticationException) {
    // 401 — token expired or invalid
} catch (e: PermissionException) {
    // 403 — API key lacks required scope
} catch (e: RateLimitException) {
    // 429 — too many requests (auto-retried if retry is configured)
} catch (e: InvalidRequestException) {
    // 400/404/409/415 — bad VIN, unsupported endpoint, etc.
} catch (e: VolvoServerException) {
    // 5xx — Volvo servers down, retry later
} catch (e: VolvoTimeoutException) {
    // Request/connect/socket timeout
} catch (e: VolvoException) {
    // Catch-all for any Volvo API error
}
```

Access error details:
```kotlin
catch (e: VolvoAPIException) {
    println("HTTP ${e.statusCode}: ${e.error.detail?.message}")
}
```

## Advanced configuration

### Retry with backoff

Retries are enabled by default (3 retries on 429/502/503/504 with exponential backoff + jitter). The client also parses `Retry-After` headers from 429 responses.

```kotlin
VolvoCarsConfig(
    apiKey = "...",
    token = "...",
    retry = RetryStrategy(
        maxRetries = 5,
        base = 2.0,
        maxDelay = 120.seconds,
        retryOnStatusCodes = setOf(429, 502, 503, 504)
    )
)
```

### Circuit breaker

Stop hitting a failing server:

```kotlin
VolvoCarsConfig(
    apiKey = "...",
    token = "...",
    circuitBreaker = CircuitBreakerConfig(
        failureThreshold = 5,     // open after 5 consecutive failures
        resetTimeout = 30.seconds // try again after 30s
    )
)
```

### Logging

```kotlin
VolvoCarsConfig(
    apiKey = "...",
    token = "...",
    logging = LoggingConfig(
        logLevel = LogLevel.Headers,
        logger = HttpLogger { println(it) },
        sanitize = true  // redacts Authorization header
    )
)
```

### Timeouts

```kotlin
VolvoCarsConfig(
    apiKey = "...",
    token = "...",
    timeout = Timeout(
        socket = 30.seconds,
        connect = 10.seconds,
        request = 60.seconds
    )
)
```

### Proxy

```kotlin
VolvoCarsConfig(
    apiKey = "...",
    token = "...",
    proxy = ProxyConfig.Http("http://proxy.corp.com:8080")
    // or: proxy = ProxyConfig.Socks("socks.corp.com", 1080)
)
```

## Resource management

Create one client instance and reuse it. Close when done:

```kotlin
// Option 1: manual close
val client = VolvoCars(config)
try {
    // use client
} finally {
    client.close()
}

// Option 2: use block (auto-closes)
VolvoCars(config).use { client ->
    val vehicles = client.getVehicleList()
}
```

The client is thread-safe — safe to share across coroutines.

## Available scopes

Each API endpoint requires specific OAuth2 scopes. Common ones:

| Scope | Access |
|-------|--------|
| `openid` | Required for all requests |
| `conve:vehicle_relation` | Vehicle list |
| `conve:commands` | List available commands |
| `conve:lock` | Lock/unlock |
| `conve:engine_status` | Engine status |
| `conve:doors_status` | Door status |
| `conve:windows_status` | Window status |
| `energy:capability:read` | Energy capabilities |
| `energy:state:read` | Energy state |

Request all scopes your app needs during the initial OAuth2 authorization.

## API rate limits

- **Status endpoints**: 100 requests/minute per Volvo ID + client ID combination
- **Command endpoints**: 10 requests/minute
- Exceeding limits returns HTTP 429 (auto-retried if retry is configured)
