# Volvo Kotlin API

Kotlin Multiplatform library for Volvo Vehicle APIs (Connected Vehicle, Energy, Extended Vehicle, Location).

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

# Publish to Maven Central
./gradlew publishAllPublicationsToMavenCentralRepository
```

## Project Structure

```
├── volvo-api-client/     # Main client library (Ktor HTTP client implementation)
├── volvo-api-core/       # Core APIs, exceptions, logging abstractions
├── OpenApi/              # OpenAPI specifications for Volvo APIs
└── gradle/               # Gradle wrapper and version catalog
```

## Architecture

### Modules
- **volvo-api-core**: Public interfaces, DSL markers, exception hierarchy, request options
- **volvo-api-client**: Implementation using Ktor client with multiplatform support

### Target Platforms
Android, JVM, iOS (x64, arm64, simulator), macOS, Linux, Windows, JS (Node.js), Wasm

### Key Packages (volvo-api-client)
- `com.github.ayastrebov.volvo.api.client` - Public API (VolvoCars, VolvoCarsConfig)
- `com.github.ayastrebov.volvo.api.client.internal` - Implementation details
- `com.github.ayastrebov.volvo.api.client.internal.api` - API endpoint implementations
- `com.github.ayastrebov.volvo.api.client.internal.http` - HTTP transport layer

### Key Packages (volvo-api-core)
- `com.github.ayastrebov.volvo.api` - DSL markers and public interfaces
- `com.github.ayastrebov.volvo.api.core` - RequestOptions
- `com.github.ayastrebov.volvo.api.exception` - Exception hierarchy
- `com.github.ayastrebov.volvo.api.logging` - Logger configuration

### API Endpoints
| API | Path |
|-----|------|
| Connected Vehicle | `connected-vehicle/v2/vehicles` |
| Energy | `energy/v2/vehicles` |
| Extended Vehicle | `extended-vehicle/v1/vehicles` |
| Location | `location/v1/vehicles` |

Base URL: `https://api.volvocars.com`

## Code Conventions

- Kotlin Multiplatform with `commonMain` source set for shared code
- Platform-specific implementations in `jvmMain`, `androidMain`, etc.
- DSL markers (`@VolvoApiDsl`, etc.) for type-safe builders
- Explicit API mode enabled for public API verification
- Internal classes/functions annotated with `@InternalVolvoApi`

### Exception Handling
Typed exceptions in `com.github.ayastrebov.volvo.api.exception`:
- `RateLimitException` (429)
- `InvalidRequestException` (400/404/409/415)
- `AuthenticationException` (401)
- `PermissionException` (403)
- `VolvoServerException` (5xx)
- `VolvoTimeoutException` (timeouts)

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
│   ├── ConnectedVehicleApiTest.kt  # Connected Vehicle API tests (27 tests)
│   ├── EnergyApiTest.kt            # Energy API tests (8 tests)
│   ├── ExtensionFunctionsTest.kt   # Extension function tests (17 tests)
│   ├── HttpClientConfigTest.kt     # HTTP client config tests (18 tests)
│   ├── HttpTransportTest.kt        # HTTP transport tests (17 tests)
│   ├── LocationApiTest.kt          # Location API tests (6 tests)
│   ├── ExceptionHandlingTest.kt    # Exception mapping tests (17 tests)
│   └── VolvoCarsConfigTest.kt      # Configuration tests (14 tests)
└── jvmTest/kotlin/com/github/ayastrebov/volvo/api/client/
    └── VolvoCarsIntegrationTest.kt # JVM integration tests (11 tests)
```

### Test Dependencies

- `kotlin-test` - Kotlin test framework
- `kotlinx-coroutines-test` - Coroutine testing utilities
- `ktor-client-mock` - HTTP client mocking

### Test Coverage

| API | Endpoints | Tests |
|-----|-----------|-------|
| Connected Vehicle | 26 | 27 |
| Energy | 2 | 8 |
| Location | 1 | 6 |
| Exception Handling | - | 17 |
| Configuration | - | 14 |
| HTTP Transport | - | 17 |
| HTTP Client Config | - | 18 |
| Extension Functions | - | 17 |
| Integration (JVM) | - | 11 |
| **Total** | **29** | **135** |

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

## Publishing Configuration

Uses [Vanniktech Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) for publishing.

### GitHub Packages

Set credentials via environment variables (recommended for CI):
```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-github-token
./gradlew publishAllPublicationsToGitHubPackagesRepository
```

Or via `local.properties`:
```properties
gpr.user=your-github-username
gpr.token=your-github-token
```

### Maven Central

Set credentials via environment variables:
```bash
export ORG_GRADLE_PROJECT_mavenCentralUsername=your-sonatype-username
export ORG_GRADLE_PROJECT_mavenCentralPassword=your-sonatype-password
export ORG_GRADLE_PROJECT_signingInMemoryKeyId=YOUR_KEY_ID
export ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=your-key-password
export ORG_GRADLE_PROJECT_signingInMemoryKey=$(base64 < ~/.gnupg/secring.gpg)
./gradlew publishAllPublicationsToMavenCentralRepository
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
