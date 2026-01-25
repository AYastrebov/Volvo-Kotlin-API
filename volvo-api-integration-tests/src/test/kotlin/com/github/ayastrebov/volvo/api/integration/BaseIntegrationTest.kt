package com.github.ayastrebov.volvo.api.integration

import com.github.ayastrebov.volvo.api.client.VolvoCars
import com.github.ayastrebov.volvo.api.client.VolvoCarsConfig
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.integration.util.IntegrationTestConfig
import com.github.ayastrebov.volvo.api.logging.LogLevel
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import kotlin.time.Duration.Companion.seconds

/**
 * Base class for integration tests that test against the real Volvo API.
 *
 * Tests in this class require valid API credentials to run.
 * If credentials are not configured, tests will be skipped.
 *
 * ## Credential Configuration
 *
 * Set credentials via environment variables:
 * ```
 * export VOLVO_API_KEY=your-vcc-api-key
 * export VOLVO_ACCESS_TOKEN=your-access-token
 * export VOLVO_VIN=your-vehicle-vin
 * ```
 *
 * Or via `local.properties` in the project root:
 * ```
 * volvo.apiKey=your-vcc-api-key
 * volvo.token=your-access-token
 * volvo.vin=your-vehicle-vin
 * ```
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseIntegrationTest {

    protected lateinit var client: VolvoCars
    protected val vin: String get() = IntegrationTestConfig.vin

    @BeforeAll
    fun setUpClient() {
        // Skip all tests in this class if credentials are not configured
        assumeTrue(
            IntegrationTestConfig.isConfigured,
            IntegrationTestConfig.missingCredentialsMessage
        )

        client = VolvoCars(
            VolvoCarsConfig(
                apiKey = IntegrationTestConfig.apiKey,
                token = IntegrationTestConfig.token,
                logging = LoggingConfig(
                    logLevel = LogLevel.All,
                    sanitize = true
                ),
                timeout = Timeout(
                    socket = 60.seconds,
                    connect = 30.seconds,
                    request = 120.seconds
                )
            )
        )

        println("Integration test client initialized for VIN: ${vin.take(3)}...${vin.takeLast(4)}")
    }

    @BeforeEach
    fun checkCredentials() {
        // Double-check credentials before each test
        assumeTrue(
            IntegrationTestConfig.isConfigured,
            IntegrationTestConfig.missingCredentialsMessage
        )
    }

    @AfterAll
    fun tearDownClient() {
        if (::client.isInitialized) {
            client.close()
            println("Integration test client closed")
        }
    }

    /**
     * Helper to log test results with masked sensitive data.
     */
    protected fun logResponse(name: String, response: Any?) {
        println("[$name] Response: $response")
    }

    /**
     * Helper to assert that a response has a successful status code (200-299).
     */
    protected fun assertSuccessStatus(status: Int?, message: String = "Expected successful status") {
        assert(status != null && status in 200..299) { "$message, but got status: $status" }
    }
}
