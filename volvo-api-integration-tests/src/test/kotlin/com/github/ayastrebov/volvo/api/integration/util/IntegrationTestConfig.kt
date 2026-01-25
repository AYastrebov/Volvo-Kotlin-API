package com.github.ayastrebov.volvo.api.integration.util

import java.io.File
import java.util.Properties

/**
 * Configuration holder for integration test credentials.
 *
 * Credentials are loaded from (in order of priority):
 * 1. System properties
 * 2. Environment variables
 * 3. local.properties file in project root
 *
 * Supports separate tokens for different APIs:
 * - volvo.token.connectedVehicle / VOLVO_TOKEN_CONNECTED_VEHICLE - for Connected Vehicle API
 * - volvo.token.energy / VOLVO_TOKEN_ENERGY - for Energy API
 * - volvo.token.location / VOLVO_TOKEN_LOCATION - for Location API
 *
 * If API-specific tokens are not set, falls back to the default token (volvo.token).
 *
 * Note: VIN is fetched dynamically from the Connected Vehicle API using getVehicleList().
 */
object IntegrationTestConfig {

    val apiKey: String by lazy { loadCredential("volvo.apiKey", "VOLVO_API_KEY") }

    /** Default token - used as fallback if API-specific tokens are not set */
    val token: String by lazy { loadCredential("volvo.token", "VOLVO_ACCESS_TOKEN") }

    /** Token for Connected Vehicle API - falls back to default token */
    val connectedVehicleToken: String by lazy {
        loadCredential("volvo.token.connectedVehicle", "VOLVO_TOKEN_CONNECTED_VEHICLE")
            .ifBlank { token }
    }

    /** Token for Energy API - falls back to default token */
    val energyToken: String by lazy {
        loadCredential("volvo.token.energy", "VOLVO_TOKEN_ENERGY")
            .ifBlank { token }
    }

    /** Token for Location API - falls back to default token */
    val locationToken: String by lazy {
        loadCredential("volvo.token.location", "VOLVO_TOKEN_LOCATION")
            .ifBlank { token }
    }

    /** List of VINs to test against */
    val vins: List<String> by lazy {
        loadCredential("volvo.vins", "VOLVO_VINS")
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    /**
     * Returns true if minimum required credentials are configured.
     * At least apiKey and at least one VIN are required.
     */
    val isConfigured: Boolean
        get() = apiKey.isNotBlank() && vins.isNotEmpty()

    /**
     * Returns a message describing which credentials are missing.
     */
    val missingCredentialsMessage: String
        get() {
            val missing = mutableListOf<String>()
            if (apiKey.isBlank()) missing.add("VOLVO_API_KEY / volvo.apiKey")
            if (vins.isEmpty()) missing.add("VOLVO_VINS / volvo.vins (comma-separated list of VINs)")
            return "Missing configuration: ${missing.joinToString(", ")}. " +
                "Set via environment variables or local.properties file."
        }

    private val localProperties: Properties by lazy {
        Properties().apply {
            val localPropertiesFile = File("local.properties")
            if (localPropertiesFile.exists()) {
                localPropertiesFile.inputStream().use { load(it) }
            }
        }
    }

    private fun loadCredential(systemPropertyKey: String, envVarKey: String): String {
        // 1. Check system properties (passed via Gradle)
        System.getProperty(systemPropertyKey)?.takeIf { it.isNotBlank() }?.let { return it }

        // 2. Check environment variables
        System.getenv(envVarKey)?.takeIf { it.isNotBlank() }?.let { return it }

        // 3. Check local.properties
        localProperties.getProperty(systemPropertyKey)?.takeIf { it.isNotBlank() }?.let { return it }

        return ""
    }
}
