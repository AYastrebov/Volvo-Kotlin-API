package com.github.ayastrebov.volvo.api.integration.util

import com.github.ayastrebov.volvo.api.client.VolvoCars
import com.github.ayastrebov.volvo.api.client.VolvoCarsConfig
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.exception.AuthenticationException
import com.github.ayastrebov.volvo.api.exception.PermissionException
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.logging.LogLevel
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

/**
 * Utility to fetch VINs from the Volvo API.
 *
 * Run with: ./gradlew :volvo-api-integration-tests:fetchVins
 *
 * Requires volvo.apiKey and volvo.token.connectedVehicle (or volvo.token) in local.properties.
 */
object FetchVins {

    @JvmStatic
    fun main(args: Array<String>) {
        val apiKey = IntegrationTestConfig.apiKey
        val token = IntegrationTestConfig.connectedVehicleToken

        if (apiKey.isBlank()) {
            printError("""
                volvo.apiKey is not configured.
                Set it in local.properties or as VOLVO_API_KEY environment variable.
            """.trimIndent())
            return
        }

        if (token.isBlank()) {
            printError("""
                volvo.token.connectedVehicle (or volvo.token) is not configured.
                Set it in local.properties or as VOLVO_TOKEN_CONNECTED_VEHICLE environment variable.
            """.trimIndent())
            return
        }

        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = apiKey,
                token = token,
                logging = LoggingConfig(
                    logLevel = LogLevel.None,
                    sanitize = true
                ),
                timeout = Timeout(
                    socket = 60.seconds,
                    connect = 30.seconds,
                    request = 120.seconds
                )
            )
        )

        try {
            val vins = runBlocking {
                val response = client.getVehicleList()
                response.data?.mapNotNull { it.vin } ?: emptyList()
            }

            if (vins.isEmpty()) {
                println("No vehicles found for this account")
                return
            }

            println()
            println("=".repeat(60))
            println("Found ${vins.size} vehicle(s)")
            println("=".repeat(60))
            println()
            println("Add this to your local.properties:")
            println()
            println("volvo.vins=${vins.joinToString(",")}")
            println()
            println("=".repeat(60))
            println()

        } catch (e: AuthenticationException) {
            printError("""
                Authentication failed (401 Unauthorized).
                Your access token has expired or is invalid.

                To generate a new token:
                1. Go to https://developer.volvocars.com/apis/
                2. Sign in and navigate to your application
                3. Generate a new access token with Connected Vehicle API scope
                4. Update local.properties with:
                   volvo.token.connectedVehicle=your-new-token
            """.trimIndent())
        } catch (e: PermissionException) {
            printError("""
                Permission denied (403 Forbidden).
                Your token doesn't have access to the Connected Vehicle API.

                Make sure your token includes the Connected Vehicle API scope.
            """.trimIndent())
        } catch (e: Exception) {
            printError("Error fetching vehicles: ${e.message}")
        } finally {
            client.close()
        }
    }

    private fun printError(message: String) {
        System.err.println()
        System.err.println("=".repeat(60))
        System.err.println("ERROR")
        System.err.println("=".repeat(60))
        System.err.println(message)
        System.err.println("=".repeat(60))
        System.err.println()
    }
}
