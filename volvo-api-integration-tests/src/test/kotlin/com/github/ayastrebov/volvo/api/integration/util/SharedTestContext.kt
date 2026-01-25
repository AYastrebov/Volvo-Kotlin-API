package com.github.ayastrebov.volvo.api.integration.util

import com.github.ayastrebov.volvo.api.client.VolvoCars
import com.github.ayastrebov.volvo.api.client.VolvoCarsConfig
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.exception.AuthenticationException
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.logging.LogLevel
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/**
 * Shared test context that initializes the client and fetches VINs once
 * for all integration tests.
 *
 * This reduces API calls by:
 * 1. Creating only one VolvoCars client instance
 * 2. Fetching the vehicle list only once
 * 3. Sharing VINs across all parameterized tests
 */
object SharedTestContext {

    private var _client: VolvoCars? = null
    private var _vins: List<String>? = null
    private var _initialized = false
    private var _initError: String? = null

    /**
     * Returns true if credentials are configured and initialization succeeded.
     */
    val isReady: Boolean
        get() {
            ensureInitialized()
            return _initialized && _initError == null
        }

    /**
     * Returns the error message if initialization failed.
     */
    val errorMessage: String?
        get() {
            ensureInitialized()
            return _initError
        }

    /**
     * Returns the shared VolvoCars client.
     * @throws IllegalStateException if not initialized or initialization failed
     */
    val client: VolvoCars
        get() {
            ensureInitialized()
            return _client ?: error("Client not initialized: $_initError")
        }

    /**
     * Returns the list of VINs fetched from the API.
     * @throws IllegalStateException if not initialized or initialization failed
     */
    val vins: List<String>
        get() {
            ensureInitialized()
            return _vins ?: error("VINs not fetched: $_initError")
        }

    /**
     * Initializes the shared context if not already initialized.
     * This is thread-safe and idempotent.
     */
    @Synchronized
    private fun ensureInitialized() {
        if (_initialized) return
        _initialized = true

        // Check credentials
        if (!IntegrationTestConfig.isConfigured) {
            _initError = IntegrationTestConfig.missingCredentialsMessage
            return
        }

        try {
            // Create client
            _client = VolvoCars(
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

            // Fetch VINs once
            _vins = runBlocking {
                val response = _client!!.getVehicleList()
                response.data?.mapNotNull { it.vin } ?: emptyList()
            }

            if (_vins!!.isEmpty()) {
                _initError = "No vehicles found for this account"
                return
            }

            println("=".repeat(60))
            println("Integration Test Setup")
            println("=".repeat(60))
            println("Found ${_vins!!.size} vehicle(s):")
            _vins!!.forEachIndexed { index, vin ->
                println("  ${index + 1}. ${vin.take(3)}...${vin.takeLast(4)}")
            }
            println("=".repeat(60))

        } catch (e: AuthenticationException) {
            _initError = """
                |
                |============================================================
                |  AUTHENTICATION FAILED (401 Unauthorized)
                |============================================================
                |  Your access token has expired or is invalid.
                |
                |  To generate a new token:
                |  1. Go to https://developer.volvocars.com/apis/
                |  2. Sign in and navigate to your application
                |  3. Generate a new access token
                |  4. Update local.properties with:
                |     volvo.token=your-new-access-token
                |
                |  Or set environment variable:
                |     export VOLVO_ACCESS_TOKEN=your-new-access-token
                |============================================================
            """.trimMargin()
            _client?.close()
            _client = null
        } catch (e: Exception) {
            _initError = "Failed to initialize: ${e.message}"
            _client?.close()
            _client = null
        }
    }

    /**
     * Closes the shared client. Call this after all tests complete.
     */
    @Synchronized
    fun shutdown() {
        _client?.close()
        _client = null
        println("Integration test client closed")
    }
}
