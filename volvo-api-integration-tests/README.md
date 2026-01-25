# Volvo API Integration Tests

This module contains integration tests that run against the real Volvo API using actual credentials.

## Prerequisites

1. A Volvo Cars developer account at [developer.volvocars.com](https://developer.volvocars.com/)
2. A registered application with API access
3. A valid access token (OAuth2)
4. A vehicle linked to your Volvo ID

## Configuration

### Environment Variables (Recommended for CI)

```bash
export VOLVO_API_KEY=your-vcc-api-key
export VOLVO_ACCESS_TOKEN=your-access-token
export VOLVO_VIN=your-vehicle-vin
```

### local.properties (For Local Development)

Create a `local.properties` file in the project root:

```properties
volvo.apiKey=your-vcc-api-key
volvo.token=your-access-token
volvo.vin=your-vehicle-vin
```

> **Note:** The `local.properties` file is gitignored and should never be committed.

## Running Tests

### Run All Integration Tests

```bash
./gradlew :volvo-api-integration-tests:test
```

### Run Specific Test Class

```bash
./gradlew :volvo-api-integration-tests:test --tests "*.ConnectedVehicleApiTest"
./gradlew :volvo-api-integration-tests:test --tests "*.EnergyApiTest"
./gradlew :volvo-api-integration-tests:test --tests "*.LocationApiTest"
```

### Run Without Destructive Commands

Tests that invoke actual vehicle commands (lock, unlock, honk, etc.) are tagged as `destructive`. To exclude them:

```bash
./gradlew :volvo-api-integration-tests:test --exclude-tags destructive
```

### Skip Integration Tests in Full Build

```bash
./gradlew build -x :volvo-api-integration-tests:test
```

### Override VIN at Runtime

```bash
./gradlew :volvo-api-integration-tests:test -Dvolvo.vin=YV1XZ00ABC1234567
```

## Test Organization

### ConnectedVehicleApiTest

- **Vehicle Information**: `getVehicleList()`, `getVehicleDetails()`
- **Status Data**: Window, door/lock, diagnostics, warnings, tyre, engine, fuel, odometer, statistics, brake status
- **Commands**: `getCommandList()`, `getCommandAccessibility()`
- **Command Invocations** (tagged `destructive`): lock, unlock, honk, flash, climatization, engine start/stop

### EnergyApiTest

- `getCapabilities()` - Check which energy features the vehicle supports
- `getEnergyState()` - Get current battery level, charging status, electric range

> **Note:** Energy API is only available for electric and hybrid vehicles.

### LocationApiTest

- `getVehicleLocation()` - Get the last known vehicle location (GeoJSON)

## Test Behavior

- **No credentials**: Tests are automatically skipped with an informative message
- **Invalid credentials**: Tests fail with authentication/permission errors
- **Rate limiting**: The Volvo API has rate limits; tests may fail if run too frequently
- **Vehicle offline**: Some endpoints may return stale data or errors if the vehicle is offline

## API Rate Limits

The Volvo API has rate limits. If you're running tests frequently:
- Consider adding delays between test runs
- Run only specific test classes
- Avoid running destructive command tests repeatedly

## Troubleshooting

### Tests Skip with "Missing credentials" Message

Verify that:
1. Environment variables are set correctly
2. OR `local.properties` exists with valid values
3. The credential values are not empty

### AuthenticationException (401)

Your access token may be expired. Tokens typically have a limited lifetime. Generate a new token using the OAuth2 flow.

### PermissionException (403)

Your API key may not have access to the requested endpoint. Check your application's subscriptions in the Volvo Developer Portal.

### RateLimitException (429)

You've exceeded the API rate limit. Wait before retrying.

### Vehicle Not Found

Ensure the VIN is correct and the vehicle is linked to your Volvo ID.
