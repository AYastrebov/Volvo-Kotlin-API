package com.github.ayastrebov.volvo.api.integration.util

import com.github.ayastrebov.volvo.api.client.VolvoCars
import com.github.ayastrebov.volvo.api.client.VolvoCarsConfig
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.logging.LogLevel
import kotlin.time.Duration.Companion.seconds

/**
 * Shared test context that initializes clients once for all integration tests.
 *
 * VINs are read from configuration (local.properties or environment variables).
 *
 * Supports separate tokens for different APIs:
 * - Connected Vehicle API: uses volvo.token.connectedVehicle (or volvo.token as fallback)
 * - Energy API: uses volvo.token.energy (or volvo.token as fallback)
 * - Location API: uses volvo.token.location (or volvo.token as fallback)
 */
object SharedTestContext {

    private var _connectedVehicleClient: VolvoCars? = null
    private var _energyClient: VolvoCars? = null
    private var _locationClient: VolvoCars? = null
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
     * Returns the client for Connected Vehicle API.
     * @throws IllegalStateException if not initialized or initialization failed
     */
    val connectedVehicleClient: VolvoCars
        get() {
            ensureInitialized()
            return _connectedVehicleClient ?: error("Connected Vehicle client not initialized: $_initError")
        }

    /**
     * Returns the client for Energy API.
     * @throws IllegalStateException if not initialized or initialization failed
     */
    val energyClient: VolvoCars
        get() {
            ensureInitialized()
            return _energyClient ?: error("Energy client not initialized: $_initError")
        }

    /**
     * Returns the client for Location API.
     * @throws IllegalStateException if not initialized or initialization failed
     */
    val locationClient: VolvoCars
        get() {
            ensureInitialized()
            return _locationClient ?: error("Location client not initialized: $_initError")
        }

    /**
     * Returns the list of VINs from configuration.
     * @throws IllegalStateException if not initialized or initialization failed
     */
    val vins: List<String>
        get() {
            ensureInitialized()
            return IntegrationTestConfig.vins
        }

    private fun createClient(token: String): VolvoCars? {
        if (token.isBlank()) return null
        return VolvoCars(
            VolvoCarsConfig(
                apiKey = IntegrationTestConfig.apiKey,
                token = token,
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
    }

    /**
     * Initializes the shared context if not already initialized.
     * This is thread-safe and idempotent.
     */
    @Synchronized
    private fun ensureInitialized() {
        if (_initialized) return
        _initialized = true

        // Check configuration
        if (!IntegrationTestConfig.isConfigured) {
            _initError = IntegrationTestConfig.missingCredentialsMessage
            System.err.println("\n" + _initError)
            return
        }

        try {
            // Create clients for each API (only if token is configured)
            _connectedVehicleClient = createClient(IntegrationTestConfig.connectedVehicleToken)
            _energyClient = createClient(IntegrationTestConfig.energyToken)
            _locationClient = createClient(IntegrationTestConfig.locationToken)

            println("=".repeat(60))
            println("Integration Test Setup")
            println("=".repeat(60))
            println("VINs configured: ${IntegrationTestConfig.vins.size}")
            IntegrationTestConfig.vins.forEachIndexed { index, vin ->
                println("  ${index + 1}. ${vin.take(3)}...${vin.takeLast(4)}")
            }
            println("Tokens configured:")
            println("  - Connected Vehicle: ${if (IntegrationTestConfig.connectedVehicleToken.isNotBlank()) "✓" else "✗"}")
            println("  - Energy: ${if (IntegrationTestConfig.energyToken.isNotBlank()) "✓" else "✗"}")
            println("  - Location: ${if (IntegrationTestConfig.locationToken.isNotBlank()) "✓" else "✗"}")
            println("=".repeat(60))

        } catch (e: Exception) {
            _initError = "Failed to initialize: ${e.message}"
            System.err.println("\n" + _initError)
            closeClients()
        }
    }

    private fun closeClients() {
        _connectedVehicleClient?.close()
        _connectedVehicleClient = null
        _energyClient?.close()
        _energyClient = null
        _locationClient?.close()
        _locationClient = null
    }

    /**
     * Closes all shared clients. Call this after all tests complete.
     */
    @Synchronized
    fun shutdown() {
        closeClients()
        println("Integration test clients closed")
    }
}
