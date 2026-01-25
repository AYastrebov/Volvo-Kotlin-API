# Volvo API Client

Kotlin Multiplatform Library for Volvo Vehicle APIs (Connected Vehicle, Energy, Extended Vehicle, Location).

## Features

- **Multiplatform Support**: Android, JVM, iOS, macOS, Linux, Windows, JS (Node.js), Wasm
- **Complete API Coverage**: Connected Vehicle, Energy, Extended Vehicle, Location APIs
- **Type-Safe**: Kotlin data classes for all request/response models
- **Coroutine-Based**: Fully async with Kotlin coroutines
- **Comprehensive Testing**: 135+ unit tests with mocked HTTP responses + integration tests

## Project Structure

```
volvo-api-core/              # Public interfaces, models, exceptions
volvo-api-client/            # Ktor HTTP client implementation
volvo-api-integration-tests/ # Integration tests with real API
```

## Run Tests

```bash
# Run all platform tests
./gradlew :volvo-api-client:allTests

# Run JVM tests only
./gradlew :volvo-api-client:jvmTest

# Run Android unit tests
./gradlew :volvo-api-client:testDebugUnitTest

# Run integration tests (requires credentials)
./gradlew :volvo-api-integration-tests:test
```

### Integration Tests

Integration tests run against the real Volvo API. Configure credentials via environment variables:

```bash
export VOLVO_API_KEY=your-vcc-api-key
export VOLVO_VINS=VIN1,VIN2
# Separate tokens for different APIs (or use VOLVO_ACCESS_TOKEN for all)
export VOLVO_TOKEN_CONNECTED_VEHICLE=your-connected-vehicle-token
export VOLVO_TOKEN_ENERGY=your-energy-token
export VOLVO_TOKEN_LOCATION=your-location-token
```

Or via `local.properties`:
```properties
volvo.apiKey=your-vcc-api-key
volvo.vins=VIN1,VIN2
volvo.token.connectedVehicle=your-connected-vehicle-token
volvo.token.energy=your-energy-token
volvo.token.location=your-location-token
```

To fetch your VINs from the Volvo API:
```bash
./gradlew :volvo-api-integration-tests:fetchVins
```

Tests skip automatically when credentials are not configured.

## Publishing

This project uses [Vanniktech Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) for publishing.

### Publish to MavenLocal

```bash
./gradlew publishToMavenLocal
```

Artifacts will be available at `~/.m2/repository/com/github/ayastrebov/volvo/`

### Publish to GitHub Packages

```bash
export GITHUB_ACTOR=your-github-username
export GITHUB_TOKEN=your-github-token
./gradlew publishAllPublicationsToGitHubPackagesRepository
```

Or set in `local.properties`:
```properties
gpr.user=your-github-username
gpr.token=your-github-token
```

### Publish to Maven Central

Configure credentials via environment variables:

```bash
export ORG_GRADLE_PROJECT_mavenCentralUsername=your-sonatype-username
export ORG_GRADLE_PROJECT_mavenCentralPassword=your-sonatype-password
export ORG_GRADLE_PROJECT_signingInMemoryKeyId=YOUR_KEY_ID
export ORG_GRADLE_PROJECT_signingInMemoryKeyPassword=your-key-password
export ORG_GRADLE_PROJECT_signingInMemoryKey=$(gpg --export-secret-keys YOUR_KEY_ID | base64)
```

Then publish:
```bash
./gradlew publishAllPublicationsToMavenCentralRepository
```

The plugin automatically handles signing and release to Maven Central via Sonatype Central Portal.

### Version Override

```bash
./gradlew publish -PVolvoApiClientDeployVersion=1.0.0
```
