<p align="center">
  <img src="volvo-logo-2021.svg" width="120" alt="Volvo"/>
</p>

<h1 align="center">Volvo Kotlin API</h1>

<p align="center">
  <strong>Kotlin Multiplatform library for Volvo Vehicle APIs</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.3-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin 2.3"/>
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License MIT"/>
  <img src="https://img.shields.io/badge/Platforms-Android%20%7C%20JVM%20%7C%20iOS%20%7C%20macOS%20%7C%20Linux%20%7C%20Windows%20%7C%20JS%20%7C%20Wasm-lightgrey" alt="Platforms"/>
</p>

---

## Features

- **Multiplatform** — Write once, run on Android, JVM, iOS, macOS, Linux, Windows, JS, and WebAssembly
- **Complete API Coverage** — Connected Vehicle, Energy, and Location APIs
- **Type-Safe** — Kotlin data classes for all request/response models with compile-time safety
- **Coroutine-Based** — Fully asynchronous with Kotlin coroutines
- **Well-Tested** — 160+ unit tests with mocked HTTP responses + integration tests against real API

## Installation

Add the dependency to your project:

**Kotlin DSL**
```kotlin
dependencies {
    implementation("com.github.ayastrebov.volvo:volvo-api-client:1.0.0")
}
```

**Groovy DSL**
```groovy
dependencies {
    implementation 'com.github.ayastrebov.volvo:volvo-api-client:1.0.0'
}
```

## Quick Start

```kotlin
val client = VolvoCars(
    apiKey = "your-vcc-api-key",
    token = "your-access-token"
)

// Get list of vehicles
val vehicles = client.getVehicleList()

// Get vehicle details
val details = client.getVehicleDetails("YV1XZ...")

// Lock vehicle
val lockResponse = client.invokeLock("YV1XZ...")

// Get energy state (electric/hybrid vehicles)
val energyState = client.getEnergyState("YV1XZ...")

// Get vehicle location
val location = client.getVehicleLocation("YV1XZ...")

client.close()
```

## Supported Platforms

| Platform | Target |
|----------|--------|
| Android | `android` |
| JVM | `jvm` |
| iOS | `iosX64`, `iosArm64`, `iosSimulatorArm64` |
| macOS | `macosX64`, `macosArm64` |
| Linux | `linuxX64` |
| Windows | `mingwX64` |
| JavaScript | `js` (Node.js) |
| WebAssembly | `wasmJs` |

## API Coverage

| API | Description | Endpoints |
|-----|-------------|-----------|
| Connected Vehicle | Vehicle info, commands, diagnostics | 25 |
| Energy | Battery state, charging | 2 |
| Location | GPS location | 1 |

**Base URL:** `https://api.volvocars.com`

## Project Structure

```
volvo-api-core/              # Public interfaces, models, exceptions
volvo-api-client/            # Ktor HTTP client implementation
volvo-api-integration-tests/ # Integration tests with real API
```

## Documentation

Generate API documentation using [Dokka](https://kotlinlang.org/docs/dokka-get-started.html):

```bash
./gradlew dokkaGenerate
```

Output: `build/dokka/html/index.html`

## Testing

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
export VOLVO_TOKEN_CONNECTED_VEHICLE=your-token
export VOLVO_TOKEN_ENERGY=your-token
export VOLVO_TOKEN_LOCATION=your-token
```

Or via `local.properties`:
```properties
volvo.apiKey=your-vcc-api-key
volvo.vins=VIN1,VIN2
volvo.token.connectedVehicle=your-token
volvo.token.energy=your-token
volvo.token.location=your-token
```

Fetch VINs from Volvo API:
```bash
./gradlew :volvo-api-integration-tests:fetchVins
```

<details>
<summary><strong>Publishing</strong></summary>

### Publish to MavenLocal

```bash
./gradlew publishToMavenLocal
```

Artifacts: `~/.m2/repository/com/github/ayastrebov/volvo/`

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

### Version Override

```bash
./gradlew publish -PVolvoApiClientDeployVersion=1.0.0
```

</details>

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

**Quick start:**

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes and add tests
4. Ensure all tests pass: `./gradlew allTests`
5. Open a Pull Request

Found a bug or have a feature request? [Open an issue](../../issues).

## License

MIT License. See [LICENSE](LICENSE.md) for details.
