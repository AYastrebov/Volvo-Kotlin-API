package com.github.ayastrebov.volvo.api.integration

import com.github.ayastrebov.volvo.api.client.VolvoCars
import com.github.ayastrebov.volvo.api.exception.PermissionException
import com.github.ayastrebov.volvo.api.exception.RateLimitException
import com.github.ayastrebov.volvo.api.integration.util.SharedTestContext
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.util.stream.Stream

/**
 * Base class for integration tests that test against the real Volvo API.
 *
 * Tests in this class require valid API credentials to run.
 * If credentials are not configured, tests will be skipped.
 *
 * ## Features
 *
 * - **Shared Clients**: Tests share VolvoCars clients (one per API)
 * - **Single VIN Fetch**: Vehicle list is fetched only once for all tests
 * - **Parameterized Tests**: Tests run for ALL vehicles in the account
 *
 * ## Credential Configuration
 *
 * Set credentials via environment variables:
 * ```
 * export VOLVO_API_KEY=your-vcc-api-key
 * export VOLVO_TOKEN_CONNECTED_VEHICLE=your-connected-vehicle-token
 * export VOLVO_TOKEN_ENERGY=your-energy-token
 * export VOLVO_TOKEN_LOCATION=your-location-token
 * ```
 *
 * Or via `local.properties` in the project root:
 * ```
 * volvo.apiKey=your-vcc-api-key
 * volvo.token.connectedVehicle=your-connected-vehicle-token
 * volvo.token.energy=your-energy-token
 * volvo.token.location=your-location-token
 * ```
 *
 * You can also use a single token for all APIs:
 * ```
 * volvo.token=your-access-token
 * ```
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest {

    /**
     * Returns the client for Connected Vehicle API.
     */
    protected val connectedVehicleClient: VolvoCars
        get() = SharedTestContext.connectedVehicleClient

    /**
     * Returns the client for Energy API.
     */
    protected val energyClient: VolvoCars
        get() = SharedTestContext.energyClient

    /**
     * Returns the client for Location API.
     */
    protected val locationClient: VolvoCars
        get() = SharedTestContext.locationClient

    @BeforeAll
    fun checkSetup() {
        assumeTrue(
            SharedTestContext.isReady,
            SharedTestContext.errorMessage ?: "Integration tests not ready"
        )
    }

    /**
     * Helper to log test results with masked sensitive data.
     */
    protected fun logResponse(name: String, vin: String, response: Any?) {
        val maskedVin = "${vin.take(3)}...${vin.takeLast(4)}"
        println("[$name] VIN=$maskedVin Response: $response")
    }

    /**
     * Helper to assert that a response has a successful status code (200-299).
     * Note: Some Volvo API responses don't include a status field in the body,
     * so null is treated as success (the HTTP response was already 200 OK).
     */
    protected fun assertSuccessStatus(status: Int?, message: String = "Expected successful status") {
        assert(status == null || status in 200..299) { "$message, but got status: $status" }
    }

    /**
     * Helper to run an API call and skip the test if permission is denied (403) or rate limited (429).
     * This allows tests to pass even when the user doesn't have access to all APIs or hits rate limits.
     */
    protected suspend fun <T> runOrSkipOnPermissionDenied(
        apiName: String,
        block: suspend () -> T
    ): T {
        return try {
            block()
        } catch (e: PermissionException) {
            assumeTrue(false, "Skipping test: No permission for $apiName (403 Forbidden)")
            throw e // unreachable, but needed for type inference
        } catch (e: RateLimitException) {
            assumeTrue(false, "Skipping test: Rate limited for $apiName (429 Too Many Requests)")
            throw e // unreachable, but needed for type inference
        }
    }

    companion object {
        /**
         * Provides all VINs for parameterized tests.
         * This is a static method that can be used with @MethodSource.
         */
        @JvmStatic
        fun allVins(): Stream<String> {
            return if (SharedTestContext.isReady) {
                SharedTestContext.vins.stream()
            } else {
                // Return empty stream if not ready - tests will be skipped by @BeforeAll
                Stream.empty()
            }
        }
    }
}
