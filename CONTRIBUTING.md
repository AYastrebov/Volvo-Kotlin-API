# Contributing to Volvo Kotlin API

Thank you for your interest in contributing! This guide will help you get started.

## Getting Started

Before contributing, please familiarize yourself with:
- [README.md](README.md) — Project overview and usage
- [CLAUDE.md](CLAUDE.md) — Technical architecture and build commands

## Development Setup

### Prerequisites

- **JDK 17+** — Required for building the project
- **Gradle** — Wrapper included (`./gradlew`)

### Building

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :volvo-api-client:build
./gradlew :volvo-api-core:build

# Clean build
./gradlew clean build
```

### Running Tests

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

## Code Style

### Kotlin Multiplatform Conventions

- Shared code goes in `commonMain` source set
- Platform-specific implementations go in `jvmMain`, `androidMain`, `iosMain`, etc.
- Use `expect`/`actual` declarations for platform-specific behavior

### Annotations

- **`@VolvoApiDsl`** — Mark DSL builder classes for type-safe builders
- **`@InternalVolvoApi`** — Mark internal APIs not intended for public use

### Explicit API Mode

This project uses Kotlin's explicit API mode. All public declarations must have:
- Explicit visibility modifiers (`public`, `internal`, `private`)
- Explicit return types for public functions and properties

### Naming Conventions

- Classes: `PascalCase`
- Functions/Properties: `camelCase`
- Constants: `SCREAMING_SNAKE_CASE`
- Packages: `lowercase`

## Submitting Issues

### Bug Reports

When reporting a bug, please include:

1. **Description** — Clear summary of the issue
2. **Steps to Reproduce** — Minimal steps to trigger the bug
3. **Expected Behavior** — What should happen
4. **Actual Behavior** — What actually happens
5. **Environment** — Kotlin version, platform, library version

### Feature Requests

For feature requests, please describe:

1. **Use Case** — What problem does this solve?
2. **Proposed Solution** — How should it work?
3. **Alternatives Considered** — Other approaches you've thought about

## Submitting Pull Requests

### Workflow

1. **Fork** the repository
2. **Create a branch** from `master`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes** with clear, focused commits
4. **Run tests** to ensure nothing is broken:
   ```bash
   ./gradlew allTests
   ```
5. **Push** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
6. **Open a Pull Request** against `master`

### PR Checklist

Before submitting, ensure:

- [ ] All tests pass (`./gradlew allTests`)
- [ ] New functionality includes tests
- [ ] Documentation is updated if needed
- [ ] No compiler warnings introduced
- [ ] Code follows existing style conventions

## Testing Requirements

### Test Utilities

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

### Test Coverage

When adding new functionality:
- Add unit tests in the appropriate `*Test.kt` file
- Test both success and error cases
- Test edge cases and boundary conditions

## Documentation

### KDoc

All public APIs should have KDoc documentation:

```kotlin
/**
 * Retrieves the list of vehicles associated with the account.
 *
 * @return List of vehicles with basic information.
 * @throws AuthenticationException if the token is invalid.
 * @throws RateLimitException if rate limit is exceeded.
 */
public suspend fun getVehicleList(): VehicleList
```

### Module Documentation

Module and package documentation is maintained in `MODULE.md` files:
- `volvo-api-core/MODULE.md`
- `volvo-api-client/MODULE.md`

## Code Review Process

1. A maintainer will review your PR
2. Address any feedback or requested changes
3. Once approved, a maintainer will merge your PR

## Commit Message Guidelines

Use clear, descriptive commit messages:

```
feat: Add support for climate control commands

- Implement startClimatization endpoint
- Add ClimateStatus response model
- Include unit tests for new functionality
```

**Prefixes:**
- `feat:` — New feature
- `fix:` — Bug fix
- `docs:` — Documentation changes
- `test:` — Test additions or fixes
- `refactor:` — Code refactoring
- `chore:` — Build/tooling changes

## Questions?

If you have questions, feel free to [open an issue](../../issues) for discussion.

Thank you for contributing!
