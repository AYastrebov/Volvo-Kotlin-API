# AGENTS.md

Kotlin Multiplatform client for the Volvo Connected Vehicle, Energy and Location APIs.
Two published modules: `volvo-api-core` (interfaces, models, exceptions, config; no Ktor) and
`volvo-api-client` (Ktor implementation). `volvo-api-integration-tests` runs against the real API.
Usage is in `README.MD`; the human contributor workflow is in `CONTRIBUTING.md`.

## Build and test

- Full build (all targets, unit tests, `apiCheck`, Dokka): `./gradlew build`
- Fast feedback while editing: `./gradlew :volvo-api-core:jvmTest :volvo-api-client:jvmTest`
- Tests on every target: `./gradlew allTests`
- After changing any public declaration: `./gradlew apiDump`, then include the `*/api/*.api` files in the change. `./gradlew build` fails until you do.
- Docs: `./gradlew dokkaGenerate` → `build/dokka/html/index.html`. `failOnWarning` is on: a broken KDoc link fails the build.
- Integration tests: `./gradlew :volvo-api-integration-tests:test`. They need `VOLVO_API_KEY`, `VOLVO_VINS` and `VOLVO_TOKEN_CONNECTED_VEHICLE` / `VOLVO_TOKEN_ENERGY` / `VOLVO_TOKEN_LOCATION` as env vars or in `local.properties`. Test tokens come from https://developer.volvocars.com/apis/docs/test-access-tokens/, last 30 minutes and are per scope. Tests skip on missing config, 403 and 429; those skips are not failures.
- Dependency updates: `./gradlew checkDependencyUpdates --no-configuration-cache` (Caupain breaks under the configuration cache); report in `build/reports/dependency-updates.html`.
- Before finishing: run the fast JVM tests, and `./gradlew build` if you touched `commonMain` (it must compile for Android, JVM, iOS, macOS, Linux, Windows, JS and Wasm).

## Dependencies

- Versions live in `gradle/libs.versions.toml` and are referenced as `libs.*`. Ask before adding a dependency.
- `volvo-api-core` must stay free of Ktor. Interfaces, models, config and exceptions go there; HTTP goes in `volvo-api-client`.

## Code rules

- Explicit API mode is on: every public declaration carries `public`; implementation details are `internal`.
- Models are `@Serializable` data classes with `@SerialName` on every property. Properties are nullable with `null` defaults so a missing field never fails the whole response (`VehicleVin.vin` is the deliberate exception).
- Enum-like strings stay `String` in models. Typed access goes through enums with an `UNKNOWN` entry and `fromString()`, as in `model/common/StatusEnums.kt`. Never use a `@Serializable` enum as a model property type.
- Response envelopes differ per API. Connected Vehicle and Location: `{status?, operationId?, data}` (the live Connected Vehicle API sends only `data`; the operation id arrives in the `vcc-api-operationid` response header). Energy v2: the bare payload, no envelope. Don't "fix" either way.
- The specs in `OpenApi/` match the developer portal but lag the live API (for example `/statistics` fields, `/fuel.batteryChargeLevel`). When a spec and a real response disagree, the real response wins; note the difference in the model KDoc and in `docs/`.
- New API methods document `Required scopes:` in the interface KDoc and add the scope to the table in `docs/authorization.md`.
- Ktor request builders: use `header()`, `basicAuth()` or `bearerAuth()` from `io.ktor.client.request`. `headers { }` without that import resolves to the `io.ktor.http` factory and silently drops the header. Default headers use `appendIfNameAbsent` so per-request headers win.
- No `@Volatile` or JVM-only APIs in `commonMain`.
- `kotlin.code.style=official`; match the surrounding code and its comment density.

## Tests

- Unit tests use the helpers in `volvo-api-client/src/commonTest/kotlin/com/github/ayastrebov/volvo/api/client/test/` (`createTestClientWithResponse`, `createTestClientWithResponses`, `RequestCapturingMockEngine`, fixtures in `TestData.kt`). Don't build raw `HttpClient`s in tests.
- Fixtures mirror real payloads. When you have a live response, copy its shape (minus anything personal) instead of inventing one.
- Add or update tests for every behaviour change. A model change also needs a serialization test in `volvo-api-core/src/commonTest`.
- Assertions: `assertEquals(expected, actual)`, `assertIs<T>`, `assertFailsWith<T>`, and `assertNotNull` into a local before dereferencing.

## Security

- API keys, tokens and VINs go in env vars or `local.properties` (git-ignored) only. Never put them in source, fixtures, docs or commit messages. Never log the `Authorization` header; `LoggingConfig.sanitize` stays on by default.
- Command endpoints (lock, unlock, engine, honk, climatization) act on real cars. Only run command integration tests against Volvo demo VINs.

## Commits and pull requests

- Commit messages: `feat:` / `fix:` / `docs:` / `test:` / `refactor:` / `chore:` prefix and an imperative summary (details in `CONTRIBUTING.md`). Don't commit or push unless asked.
- Before a PR: `./gradlew build` is green, `.api` dumps are current, and docs and KDoc reflect any public change.

## Where things are

- `OpenApi/` — Volvo's published OpenAPI specs (endpoint source of truth, with the caveat above)
- `docs/` — per-endpoint reference; `docs/authorization.md` holds the scope table
- `.claude/skills/volvo-kotlin-api/SKILL.md` — step-by-step procedures: adding an endpoint, a config class, verifying models against the live API, releasing
- `.claude/skills/volvo-api-integration/SKILL.md` — guide for consumers of the library
