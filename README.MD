<p align="center">
  <img src="volvo-logo-2021.svg" width="120" alt="Volvo"/>
</p>

<h1 align="center">Volvo Kotlin API</h1>

<p align="center">
  <strong>Kotlin Multiplatform library for Volvo Vehicle APIs</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin 2.3.21"/>
  <img src="https://img.shields.io/badge/JDK-21-orange" alt="JDK 21"/>
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

Add the GitHub Packages repository and dependency to your project:

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/AYastrebov/Volvo-Kotlin-API")
        credentials {
            username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
            password = providers.gradleProperty("gpr.token").orNull ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("com.github.ayastrebov.volvo:volvo-api-client:0.5.2")
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
| iOS | `iosArm64`, `iosSimulatorArm64` |
| macOS | `macosArm64` |
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

## Artifact Verification

Published artifacts are signed with GPG. To verify signatures, import the public key:

```bash
gpg --keyserver keyserver.ubuntu.com --recv-keys D0B3B155
```

Or import manually:

<details>
<summary>Public Key</summary>

```
Fingerprint: 7E64 1A77 C010 DEB3 0E77  0A26 E324 0D17 D0B3 B155
Key ID: D0B3B155
```

```bash
gpg --import <<'EOF'
-----BEGIN PGP PUBLIC KEY BLOCK-----

mQINBGn7XzcBEACqtfHxqRBbHwPZF+SkcP9ns0lxLqRiONZge1NIwfjvlGELPpWv
Rf2e6rGGlu17ppqmpWAN9dK2y+dHogEDXcOxquGNsQAb1tL72yfxPam7WihLkaTJ
tUo6fX7L/wGZSJeV9Us+SasvN3xqZsVtxb0k5qAu0083VlDFcztCaP/pse5AEJ3M
F4EbhIG47EofFMaSbY0ysyL7PvgcVfqxUmGeZH5rufR5Sl9a3JNF65pOryyQWh3J
PPzqWTXGD3sbG4HKaKIdIhqFQYvC5crf+mhZFxPnZuN3X8z+5Ccc/+Hvuw4TMgEl
U50Jqg2YQilabtsputTzoxfRCcibov5S1oiZd+dijYQ9PotGupp5TCYb/UIqOSP9
Wh6xdzXZKMVjJxVYrJesvrt5Zrb672tKZutu17mOaZvt3gF4/B33uEX81Fjr6yKT
KTqSQYqBDC+tgX9xQ8R5N9mNNlmJvG181q7MqjvKVEG4mwzJ5ELj81w1va7y7wN9
QTv4o9WTAEV9n18HVBXJkuYb5eeHhflIcNvuxxWLPx2kU8FIFsXvqWoCloRRXtmv
q5D9gvcCstX4nHz4Bwp5TmxNl5zMuLMF7IohO+oMRgXwhMSeqtUyDY1gzYTiS/iH
tWLaC/BWfduw/UtmpACl8PeSo7FiL3giIBWyJkBxXVTiY13WFf14zSiNUwARAQAB
tD9BbmRyZXkgWWFzdHJlYm92IChNYXZlbiBzaWduaW5nIGtleSBSU0EpIDxheWFz
dHJlYm92QGdtYWlsLmNvbT6JAm4EEwEIAFgWIQR+ZBp3wBDesw53CibjJA0X0LOx
VQUCaftfNxsUgAAAAAAEAA5tYW51MiwyLjUrMS4xMiwwLDMDGy8EBQsJCAcCAiIC
BhUKCQgLAgQWAgMBAh4HAheAAAoJEOMkDRfQs7FVm1MP/isy1rZCh3L4p7PbUSTQ
OMJs0QS+fC8YwFJTG1B6ZJmRPJWGoniOtG9pG1I2JKWYlUM32jFuUAdNjFehbL8q
pBzsdEuAn7c586d6t+tHghFBV5JHf8G9006UPHtDXRugNpU4AiKjQ543CaF9kPIX
Chyd99NXbKFkGrH2ILjL9NHkfBh5DsaiHvymIPIgqLh4lexMD9Az5FKD3/jsF+IW
+NxhchGGOz1nHCKEPmXZYWVQhC2YI/GWLOlcqsn8A9RJmxJFICuKyHbwMy3zcayo
K9D1Q3D4ShbiRGL32cjblTwZrehZjc5znOugW3poa9MXJOT7Lv3orkEOB9HPKAVl
wgoS5g8BbbYgKDniFZIAyfVlW+Zmmeyt2VGdNOu2kQbc166MsKd6neUWN1FVCMoy
oIpvFnWoVtAhghhRNa6g7YXtgS9CcqH2wHUsOmOCiFiGej9iBTcRqjQWBCkkrrii
4KZRus+VL6ihI4IDLplu1KNc1dk+6z3lMlg9EKE5lmr8cbkKWv5I3fXPUGYbHDqn
OUcDq4fGLMyLrBbD2469BuH1vkn6Tfr7zCWb4JlCur+/oH1sa9va5rS+nSmOc4NY
sIRZEunN6f9TpE4Iik5YrpS6y+OnKCSICG5RycJFp9msyDl745HUYN0sRD5U6UuC
jnazxtucoAKo26CUAGUsRpjO
=tPzu
-----END PGP PUBLIC KEY BLOCK-----
EOF
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
