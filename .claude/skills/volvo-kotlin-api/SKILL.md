---
name: volvo-kotlin-api
description: "Development guide for the Volvo Kotlin API library — a Kotlin Multiplatform client for Volvo Vehicle APIs (Connected Vehicle, Energy, Location). Use this skill when adding new API endpoints, fixing bugs, writing tests, updating models, modifying the HTTP transport layer, working with OAuth2 authentication, or making any changes to the volvo-api-core or volvo-api-client modules. Also trigger when the user mentions Volvo API, vehicle commands, VIN, energy state, or any Ktor client configuration in this project."
---

# Volvo Kotlin API — Development Guide

## Architecture at a glance

```
volvo-api-core    → Public interfaces, models, exceptions, config (no HTTP dependency)
volvo-api-client  → Ktor HTTP implementation, internal transport, API delegation
```

The separation matters: **core** has zero Ktor dependencies and defines the contract. **client** implements it. When adding a new feature, ask yourself which module it belongs in — if it doesn't need HTTP, it goes in core.

The client uses Kotlin's delegation pattern: `VolvoCarsApi` delegates to `ConnectedVehicleApi`, `EnergyApi`, `LocationApi` via `by` keyword. Each API class takes an `HttpRequester` and builds requests against it.

## Adding a new API endpoint

This is the most common task. Follow this exact sequence:

### 1. Add the response model (volvo-api-core)

Create or update a data class in `volvo-api-core/src/commonMain/kotlin/.../model/{api-domain}/`:

```kotlin
@Serializable
public data class NewFeatureResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: NewFeatureData? = null
)
```

All model fields should be nullable with defaults — the API may omit them. Use `@SerialName` to match the JSON field names exactly.

### 2. Add the interface method (volvo-api-core)

Add the suspend function to the appropriate interface in `volvo-api-core/src/commonMain/kotlin/.../api/`:

```kotlin
public suspend fun getNewFeature(
    vin: String,
    requestOptions: RequestOptions? = null
): NewFeatureResponse
```

Every endpoint method follows this pattern: takes a `vin`, optional `requestOptions`, returns a typed response. POST commands also take a request body parameter.

### 3. Implement the endpoint (volvo-api-client)

Add the implementation in `volvo-api-client/src/commonMain/kotlin/.../internal/api/`:

```kotlin
override suspend fun getNewFeature(vin: String, requestOptions: RequestOptions?): NewFeatureResponse {
    return requester.perform(typeInfo<NewFeatureResponse>()) { client ->
        client.get("${ApiConfig.CONNECTED_VEHICLE_API}/$vin/new-feature") {
            requestOptions(requestOptions)
        }
    }
}
```

For POST commands, use `client.post` with a body.

### 4. Add the test (volvo-api-client/src/commonTest)

```kotlin
@Test
fun getNewFeature_returnsData() = runTest {
    val client = createTestClientWithResponse("""{"status": 200, "data": {...}}""")
    val result = client.getNewFeature(TestData.TEST_VIN)
    assertNotNull(result.data)
}
```

### 5. Update API dump

```bash
./gradlew apiDump   # regenerate .api files
./gradlew build     # verify apiCheck passes
```

## Adding a new config class (volvo-api-core)

Config classes live in core because they have no HTTP dependency. Pattern:

```kotlin
public data class NewConfig(
    public val someSetting: Int = 5,
    public val anotherSetting: Duration = 30.seconds,
) {
    init {
        require(someSetting > 0) { "someSetting must be positive, was $someSetting" }
    }
}
```

Then add it as an optional parameter on `VolvoCarsConfig` and wire it in `createHttpClient()`.

## Test patterns

Use the existing test utilities — don't create new HTTP clients manually:

```kotlin
// Single response mock
val client = createTestClientWithResponse(responseJson)

// Multi-path mock
val client = createTestClientWithResponses(mapOf(
    "path/endpoint" to MockResponse(HttpStatusCode.OK, json)
))

// Capture and verify requests
val engine = RequestCapturingMockEngine(MockResponse(HttpStatusCode.OK, json))
val client = createTestClient(engine.engine)
client.doSomething()
assertEquals(HttpMethod.Post, engine.requests.first().method)
```

Assertion conventions — follow these strictly:
- `assertIs<Type>(value)` for type checks (never `assertTrue(x is T)`)
- `assertEquals(expected, actual)` for equality (never `assertTrue(x == y)`)
- `assertFailsWith<ExceptionType> { ... }` for expected failures
- Extract nullable to local val + `assertNotNull` before accessing properties

## Exception handling

All HTTP errors are mapped in `HttpTransport.handleException()`:
- 401 → `AuthenticationException`
- 403 → `PermissionException`
- 429 → `RateLimitException`
- Other 4xx → `InvalidRequestException`
- 5xx → `VolvoServerException`
- Timeout → `VolvoTimeoutException`
- Network I/O → `GenericIOException`

If the circuit breaker is open, `HttpTransport.checkCircuitBreaker()` throws `RateLimitException` before making any HTTP call.

## OAuth2 flow

Authentication supports two mutually exclusive modes (validated in `VolvoCarsConfig.init`):
- `oauth: OAuthConfig` — Ktor's bearer auth with `refreshTokens {}` that calls Volvo ID's token endpoint. Uses Basic auth with `client_id:client_secret`. Supports refresh token rotation via `onTokensRefreshed` callback.
- `token: String` — Static bearer token, no refresh.

When modifying auth, remember the token endpoint is `https://volvoid.eu.volvocars.com/as/token.oauth2` and uses `application/x-www-form-urlencoded` with `grant_type=refresh_token`.

## Key build commands

```bash
./gradlew build              # build + test + apiCheck
./gradlew apiDump            # regenerate binary compatibility .api files
./gradlew apiCheck           # verify no breaking API changes
./gradlew dokkaGenerate      # generate aggregated docs (failOnWarning enabled)
./gradlew allTests           # multiplatform tests
./gradlew :volvo-api-client:jvmTest  # fast JVM-only tests
```

## Publishing a release

```bash
git tag v1.0.0 && git push origin v1.0.0
```

This triggers the `publish.yml` workflow which runs tests, publishes to GitHub Packages (with version from tag), creates a GitHub Release, and deploys Dokka docs to GitHub Pages. GPG signing uses RSA key (D0B3B155) stored base64-encoded in secrets.

## Things to watch out for

- **Explicit API mode** is on — every public declaration needs an explicit visibility modifier
- **Binary compatibility** — `.api` files are checked in CI. Run `apiDump` after any public API change
- **No `@Volatile`** in commonMain — JS/WasmJS targets don't support it
- **`failOnWarning`** on Dokka — broken KDoc links fail the build. Use fully qualified names for extension property references
- **Consumer ProGuard rules** — update `consumer-rules.pro` if adding new `@Serializable` model packages
- **KMP target coverage** — code in `commonMain` must compile for all 9 targets (Android, JVM, iOS arm64, iOS simulator, macOS, Linux, Windows, JS, WasmJS)
