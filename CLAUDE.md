# Volvo Kotlin API

Kotlin Multiplatform library for Volvo Vehicle APIs (Connected Vehicle, Energy, Location).

## Build Commands

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :volvo-api-client:build
./gradlew :volvo-api-core:build

# Run tests
./gradlew test
./gradlew :volvo-api-client:allTests

# Clean build
./gradlew clean build

# Publish to local Maven
./gradlew publishToMavenLocal

# Publish to GitHub Packages
./gradlew publishAllPublicationsToGitHubPackagesRepository
```

## Project Structure

```
├── volvo-api-client/            # Main client library (Ktor HTTP client implementation)
├── volvo-api-core/              # Core APIs, exceptions, logging abstractions
├── volvo-api-integration-tests/ # Integration tests against real Volvo API
├── OpenApi/                     # OpenAPI specifications for Volvo APIs
└── gradle/                      # Gradle wrapper and version catalog
```

## Architecture

### Modules
- **volvo-api-core**: Public interfaces, exception hierarchy, request options
- **volvo-api-client**: Implementation using Ktor client with multiplatform support

### Target Platforms
Android, JVM, iOS (arm64, simulator), macOS (arm64), Linux, Windows, JS (Node.js), Wasm

### Key Packages (volvo-api-client)
- `com.github.ayastrebov.volvo.api.client` - Public API (VolvoCars, VolvoCarsConfig)
- `com.github.ayastrebov.volvo.api.client.internal` - Implementation details
- `com.github.ayastrebov.volvo.api.client.internal.api` - API endpoint implementations
- `com.github.ayastrebov.volvo.api.client.internal.http` - HTTP transport layer

### Key Packages (volvo-api-core)
- `com.github.ayastrebov.volvo.api.api` - Public API interfaces (ConnectedVehicle, Energy, Location)
- `com.github.ayastrebov.volvo.api.core` - RequestOptions, OAuthConfig, RetryStrategy, ProxyConfig, LoggingConfig
- `com.github.ayastrebov.volvo.api.exception` - Exception hierarchy (sealed VolvoException)
- `com.github.ayastrebov.volvo.api.logging` - HttpLogger, LogLevel
- `com.github.ayastrebov.volvo.api.http` - Timeout configuration
- `com.github.ayastrebov.volvo.api.model` - Request/response data models

### API Endpoints
| API | Path |
|-----|------|
| Connected Vehicle | `connected-vehicle/v2/vehicles` |
| Energy | `energy/v2/vehicles` |
| Location | `location/v1/vehicles` |

Base URL: `https://api.volvocars.com`

## Code Conventions

- Kotlin Multiplatform with `commonMain` source set for shared code
- Platform-specific implementations in `jvmMain`, `androidMain`, etc.
- Explicit API mode enabled for public API verification
- Internal classes/functions use Kotlin's `internal` visibility modifier

### Authentication
Two modes via `VolvoCarsConfig` (mutually exclusive, validated at init):
- **OAuth2** (`oauth: OAuthConfig`): Automatic token refresh via Volvo ID token endpoint. Supports refresh token rotation with `onTokensRefreshed` callback.
- **Static token** (`token: String`): For testing with test access tokens. No auto-refresh.

Token endpoint: `https://volvoid.eu.volvocars.com/as/token.oauth2`

### Exception Handling
Typed exceptions in `com.github.ayastrebov.volvo.api.exception`:
- `RateLimitException` (429)
- `InvalidRequestException` (400/404/409/415)
- `AuthenticationException` (401)
- `PermissionException` (403)
- `VolvoServerException` (5xx)
- `VolvoTimeoutException` (timeouts)

### Production Features
- **Binary compatibility validator** (`kotlinx-binary-compatibility-validator`) — `.api` dump files in `api/` directories, checked by `apiCheck` task
- **Consumer ProGuard rules** — `consumer-rules.pro` in both modules for Android R8 compatibility
- **Retry with jitter** — Exponential backoff with ±25% randomization to prevent retry storms
- **Dokka failOnWarning** — KDoc link errors fail the build
- **External doc links** — Ktor, kotlinx-coroutines, kotlinx-serialization types link to upstream docs

## Testing

```bash
# Run all tests
./gradlew allTests

# Run JVM tests only
./gradlew :volvo-api-client:jvmTest

# Run JS tests
./gradlew :volvo-api-client:jsTest

# Run Android unit tests
./gradlew :volvo-api-client:testDebugUnitTest

# Run WasmJS tests
./gradlew :volvo-api-client:wasmJsTest
```

### Test Structure

Test sources: `commonTest`, `jvmTest`, `jsTest`, `wasmJsTest`, `androidUnitTest`

```
volvo-api-client/src/
├── commonTest/kotlin/com/github/ayastrebov/volvo/api/client/
│   ├── test/
│   │   ├── MockHttpEngine.kt       # Mock HTTP engine utilities
│   │   ├── TestClientFactory.kt    # Test client factory functions
│   │   └── TestData.kt             # Test fixtures and constants
│   ├── ConnectedVehicleApiTest.kt  # Connected Vehicle API tests (28 tests)
│   ├── EnergyApiTest.kt            # Energy API tests (7 tests)
│   ├── ExceptionHandlingTest.kt    # Exception mapping tests (23 tests)
│   ├── ExtensionFunctionsTest.kt   # Extension function tests (21 tests)
│   ├── HttpClientConfigTest.kt     # HTTP client config tests (17 tests)
│   ├── HttpTransportTest.kt        # HTTP transport tests (17 tests)
│   ├── LocationApiTest.kt          # Location API tests (6 tests)
│   ├── RetryBehaviorTest.kt        # Retry logic tests (12 tests)
│   ├── TimeoutBehaviorTest.kt      # Timeout handling tests (13 tests)
│   └── VolvoCarsConfigTest.kt      # Configuration tests (12 tests)
└── jvmTest/kotlin/com/github/ayastrebov/volvo/api/client/
    └── VolvoCarsClientTest.kt      # JVM client lifecycle tests (5 tests)
```

### Test Dependencies

- `kotlin-test` - Kotlin test framework
- `kotlinx-coroutines-test` - Coroutine testing utilities
- `ktor-client-mock` - HTTP client mocking

### Test Coverage

| API | Endpoints | Tests |
|-----|-----------|-------|
| Connected Vehicle | 25 | 28 |
| Energy | 2 | 7 |
| Location | 1 | 6 |
| Exception Handling | - | 23 |
| Configuration | - | 12 |
| HTTP Transport | - | 17 |
| HTTP Client Config | - | 17 |
| Extension Functions | - | 21 |
| Retry Behavior | - | 12 |
| Timeout Behavior | - | 13 |
| Client (JVM) | - | 5 |
| **Total** | **28** | **161** |

### Writing Tests

Use the test utilities in `commonTest/kotlin/.../test/`:

```kotlin
// Create a mock client with a single response
val client = createTestClientWithResponse(ConnectedVehicleFixtures.vehicleListResponse)

// Create a mock client with multiple path-based responses
val client = createTestClientWithResponses(mapOf(
    "connected-vehicle/v2/vehicles" to MockResponse(HttpStatusCode.OK, json)
))

// Create a request-capturing mock for verification
val capturingEngine = RequestCapturingMockEngine(
    MockResponse(HttpStatusCode.OK, responseJson)
)
val client = createTestClient(capturingEngine.engine)
// ... make request ...
assertEquals(HttpMethod.Post, capturingEngine.requests.first().method)
```

### Integration Tests

Integration tests in `volvo-api-integration-tests/` run against the real Volvo API.

```bash
# Run integration tests
./gradlew :volvo-api-integration-tests:test

# Fetch VINs from Volvo API (prints config for local.properties)
./gradlew :volvo-api-integration-tests:fetchVins
```

#### Configuration

Via environment variables:
```bash
export VOLVO_API_KEY=your-vcc-api-key
export VOLVO_VINS=VIN1,VIN2
export VOLVO_TOKEN_CONNECTED_VEHICLE=your-token
export VOLVO_TOKEN_ENERGY=your-token
export VOLVO_TOKEN_LOCATION=your-token
```

Via `local.properties`:
```properties
volvo.apiKey=your-vcc-api-key
volvo.vins=VIN1,VIN2
volvo.token.connectedVehicle=your-token
volvo.token.energy=your-token
volvo.token.location=your-token
```

Tests skip automatically when credentials are not configured or when API returns permission errors.

## Publishing Configuration

Uses [Vanniktech Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) for publishing.

### GitHub Packages (CI)

Automated via `publish.yml` workflow: tag with `v*` to auto-publish + create GitHub Release + deploy Dokka docs.

```bash
git tag v1.0.0 && git push origin v1.0.0
```

Secrets required: `SIGNING_KEY_ID` (RSA), `SIGNING_PASSWORD`, `SIGNING_SECRET_KEY` (base64-encoded).
Secret key is base64-decoded at runtime in CI (matches KTelegram pattern).

### Maven Local

```bash
./gradlew publishToMavenLocal
```

### Version Override
```bash
./gradlew publish -PVolvoApiClientDeployVersion=1.0.0
```

## Documentation

API documentation is generated using [Dokka](https://kotlinlang.org/docs/dokka-get-started.html).

### Generate Documentation

```bash
# Generate all documentation (aggregated)
./gradlew dokkaGenerate

# Generate documentation for specific module
./gradlew :volvo-api-core:dokkaGeneratePublicationHtml
./gradlew :volvo-api-client:dokkaGeneratePublicationHtml
```

### Output Locations

- **Aggregated docs**: `build/dokka/html/index.html`
- **Core module**: `volvo-api-core/build/dokka/html/index.html`
- **Client module**: `volvo-api-client/build/dokka/html/index.html`

### Module Documentation

Module and package documentation is maintained in `MODULE.md` files:
- `volvo-api-core/MODULE.md` - Core module and package descriptions
- `volvo-api-client/MODULE.md` - Client module and package descriptions

Documentation follows Dokka's format: https://kotlinlang.org/docs/dokka-module-and-package-docs.html
