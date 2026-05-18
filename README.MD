<p align="center">
  <img src="volvo-logo-2021.svg" width="120" alt="Volvo"/>
</p>

<h1 align="center">Volvo Kotlin API</h1>

<p align="center">
  <strong>Kotlin Multiplatform client for Volvo Vehicle APIs</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin 2.3.21"/>
  <img src="https://img.shields.io/badge/JDK-21-orange" alt="JDK 21"/>
  <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License MIT"/>
  <img src="https://img.shields.io/badge/Platforms-Android%20%7C%20JVM%20%7C%20iOS%20%7C%20macOS%20%7C%20Linux%20%7C%20Windows%20%7C%20JS%20%7C%20Wasm-lightgrey" alt="Platforms"/>
</p>

---

A Kotlin Multiplatform library that wraps the Volvo Connected Vehicle, Energy, and Location APIs. Runs on Android, JVM, iOS, macOS, Linux, Windows, JS, and WebAssembly from a single codebase.

Handles OAuth2 token refresh (with Volvo ID's refresh token rotation), retries with jitter, circuit breaking, and typed error mapping. 280+ unit tests, binary compatibility checks, and Android ProGuard rules.

## Installation

The library is on GitHub Packages. Add the repository and a Ktor engine for your platform:

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
    implementation("com.github.ayastrebov.volvo:volvo-api-client:0.6.0")

    // Pick a Ktor engine for your platform:
    implementation("io.ktor:ktor-client-okhttp:3.4.3")    // JVM / Android
    // implementation("io.ktor:ktor-client-darwin:3.4.3")  // iOS / macOS
    // implementation("io.ktor:ktor-client-js:3.4.3")      // JS (Node.js)
}
```

You need a Ktor engine at runtime. Without one the client crashes on startup. See [Ktor engines](https://ktor.io/docs/client-engines.html) for the full list.

## Usage

```kotlin
// With OAuth2 token refresh (recommended for production)
val client = VolvoCars(
    VolvoCarsConfig(
        apiKey = "your-vcc-api-key",
        oauth = OAuthConfig(
            accessToken = storedAccessToken,
            refreshToken = storedRefreshToken,
            clientId = "your-client-id",
            clientSecret = "your-client-secret",
            onTokensRefreshed = { accessToken, refreshToken ->
                // Volvo ID rotates refresh tokens on each use -- persist both
                tokenStorage.save(accessToken, refreshToken)
            }
        )
    )
)

// Or with a test token (no refresh, expires eventually)
// val client = VolvoCars(apiKey = "your-key", token = "your-test-token")

val vehicles = client.getVehicleList()
val location = client.getVehicleLocation("YV1XZ...")
client.invokeLock("YV1XZ...")

client.close()
```

## Supported platforms

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

## API coverage

| API | What it does | Endpoints |
|-----|-------------|-----------|
| Connected Vehicle | Vehicle info, remote commands, diagnostics | 25 |
| Energy | Battery level, charging status | 2 |
| Location | Last known GPS position | 1 |

Base URL: `https://api.volvocars.com`

## Project structure

```
volvo-api-core/              # Public interfaces, models, exceptions
volvo-api-client/            # Ktor HTTP implementation
volvo-api-integration-tests/ # Tests against the real Volvo API
```

## Documentation

[ayastrebov.github.io/Volvo-Kotlin-API](https://ayastrebov.github.io/Volvo-Kotlin-API/) has the full API reference, generated with Dokka.

To build locally: `./gradlew dokkaGenerate` (output in `build/dokka/html/`).

## Testing

```bash
./gradlew :volvo-api-client:allTests          # all platforms
./gradlew :volvo-api-client:jvmTest           # JVM only (fastest)
./gradlew :volvo-api-integration-tests:test   # real API (needs credentials)
```

### Integration tests

Integration tests hit the real Volvo API and skip automatically when credentials are missing. Set them via environment variables or `local.properties`:

```bash
export VOLVO_API_KEY=your-vcc-api-key
export VOLVO_VINS=VIN1,VIN2
export VOLVO_TOKEN_CONNECTED_VEHICLE=your-token
export VOLVO_TOKEN_ENERGY=your-token
export VOLVO_TOKEN_LOCATION=your-token
```

<details>
<summary><strong>Publishing</strong></summary>

Tag a release to publish automatically:

```bash
git tag v1.0.0 && git push origin v1.0.0
```

This runs tests, publishes to GitHub Packages, creates a GitHub Release, and deploys docs.

For local testing: `./gradlew publishToMavenLocal`

Version override: `./gradlew publish -PVolvoApiClientDeployVersion=1.0.0`

</details>

## Authentication

Two modes, mutually exclusive:

**OAuth2 with automatic refresh** -- pass `OAuthConfig` with your client credentials. The client calls the [Volvo ID token endpoint](https://developer.volvocars.com/apis/docs/authorisation/) on 401 and rotates the refresh token. Use `onTokensRefreshed` to save both new tokens (Volvo invalidates the old refresh token immediately).

**Static token** -- pass `token` directly. Works with [test access tokens](https://developer.volvocars.com/apis/docs/test-access-tokens/) from the Developer Portal. No auto-refresh.

## Configuration

Pass `VolvoCarsConfig` for full control:

```kotlin
VolvoCars(
    VolvoCarsConfig(
        apiKey = "your-key",
        token = "your-test-token",
        retry = RetryStrategy(maxRetries = 5, maxDelay = 120.seconds),
        circuitBreaker = CircuitBreakerConfig(failureThreshold = 5, resetTimeout = 30.seconds),
        logging = LoggingConfig(logLevel = LogLevel.Headers),
        timeout = Timeout(socket = 30.seconds, connect = 10.seconds),
        proxy = ProxyConfig.Http("http://proxy.corp.com:8080"),
        httpClientConfig = { /* extra Ktor config */ }
    )
)
```

The retry logic parses `Retry-After` headers from 429 responses and falls back to exponential backoff with jitter.

## Artifact verification

Published artifacts are GPG-signed (key `D0B3B155`):

```bash
gpg --keyserver keyserver.ubuntu.com --recv-keys D0B3B155
```

<details>
<summary>Full public key</summary>

```
Fingerprint: 7E64 1A77 C010 DEB3 0E77  0A26 E324 0D17 D0B3 B155
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

See [CONTRIBUTING.md](CONTRIBUTING.md) for the full guide.

1. Fork the repository
2. Create a feature branch
3. Add tests for your changes
4. Run `./gradlew allTests` to verify
5. Open a pull request

Found a bug? [Open an issue](../../issues).

## License

MIT. See [LICENSE](LICENSE.md).
