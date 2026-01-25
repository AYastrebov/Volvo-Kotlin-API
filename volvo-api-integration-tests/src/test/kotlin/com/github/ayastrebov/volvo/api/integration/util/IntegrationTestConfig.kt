package com.github.ayastrebov.volvo.api.integration.util

import java.io.File
import java.util.Properties

/**
 * Configuration holder for integration test credentials.
 *
 * Credentials are loaded from (in order of priority):
 * 1. System properties (volvo.apiKey, volvo.token)
 * 2. Environment variables (VOLVO_API_KEY, VOLVO_ACCESS_TOKEN)
 * 3. local.properties file in project root
 *
 * Note: VIN is fetched dynamically from the API using getVehicleList().
 */
object IntegrationTestConfig {

    val apiKey: String by lazy { loadCredential("volvo.apiKey", "VOLVO_API_KEY") }
    val token: String by lazy { loadCredential("volvo.token", "VOLVO_ACCESS_TOKEN") }

    /**
     * Returns true if all required credentials are configured.
     */
    val isConfigured: Boolean
        get() = apiKey.isNotBlank() && token.isNotBlank()

    /**
     * Returns a message describing which credentials are missing.
     */
    val missingCredentialsMessage: String
        get() {
            val missing = mutableListOf<String>()
            if (apiKey.isBlank()) missing.add("VOLVO_API_KEY")
            if (token.isBlank()) missing.add("VOLVO_ACCESS_TOKEN")
            return "Missing credentials: ${missing.joinToString(", ")}. " +
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
