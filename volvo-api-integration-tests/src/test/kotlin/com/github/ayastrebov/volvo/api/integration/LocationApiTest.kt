package com.github.ayastrebov.volvo.api.integration

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the Location API.
 *
 * These tests call the real Volvo API and require valid credentials.
 * Tests are parameterized to run for ALL vehicles in the user's account.
 * Tests will be skipped if the user doesn't have Location API access.
 */
@DisplayName("Location API Integration Tests")
class LocationApiTest : BaseIntegrationTest() {

    @ParameterizedTest(name = "Get vehicle location for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Get vehicle location returns GeoJSON Feature")
    fun getVehicleLocation_returnsLocation(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            locationClient.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", vin, response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data, "Location response should not be null")
        assertEquals("Feature", response.data!!.type, "Response should be a GeoJSON Feature")
    }

    @ParameterizedTest(name = "Location response contains geometry for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Location response contains geometry")
    fun locationResponse_containsGeometry(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            locationClient.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", vin, response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data?.geometry, "Geometry should not be null")
        assertEquals("Point", response.data!!.geometry!!.type, "Geometry type should be Point")
    }

    @ParameterizedTest(name = "Location coordinates are valid for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Location coordinates are valid")
    fun locationCoordinates_areValid(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            locationClient.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", vin, response)
        assertSuccessStatus(response.status)

        val geometry = response.data?.geometry
        assertNotNull(geometry, "Geometry should not be null")

        val longitude = geometry.longitude
        val latitude = geometry.latitude

        assertNotNull(longitude, "Longitude should not be null")
        assertNotNull(latitude, "Latitude should not be null")

        // Validate coordinate ranges
        assertTrue(
            longitude >= -180.0 && longitude <= 180.0,
            "Longitude should be between -180 and 180, but was $longitude"
        )
        assertTrue(
            latitude >= -90.0 && latitude <= 90.0,
            "Latitude should be between -90 and 90, but was $latitude"
        )

        println("Vehicle location for $vin: ($latitude, $longitude)")
    }

    @ParameterizedTest(name = "Location response contains properties for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Location response contains properties")
    fun locationResponse_containsProperties(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            locationClient.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", vin, response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data?.properties, "Properties should not be null")

        // Log available properties for debugging
        // Properties is a Map<String, String>
        response.data?.properties?.let { props ->
            println("Location properties for $vin:")
            props.forEach { (key, value) ->
                println("  - $key: $value")
            }
        }
    }

    @ParameterizedTest(name = "Location heading is within valid range for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Location heading is within valid range if present")
    fun locationHeading_isWithinValidRange(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            locationClient.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", vin, response)
        assertSuccessStatus(response.status)

        // Properties is a Map<String, String>, so we need to parse heading as Double
        response.data?.properties?.get("heading")?.toDoubleOrNull()?.let { heading ->
            assertTrue(
                heading >= 0.0 && heading < 360.0,
                "Heading should be between 0 and 360 degrees, but was $heading"
            )
            println("Vehicle heading for $vin: $heading degrees")
        }
    }

    @ParameterizedTest(name = "Location timestamp is present for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Location timestamp is present if available")
    fun locationTimestamp_isPresent(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            locationClient.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", vin, response)
        assertSuccessStatus(response.status)

        // Properties is a Map<String, String>
        val timestamp = response.data?.properties?.get("timestamp")
        if (timestamp != null) {
            assertTrue(timestamp.isNotBlank(), "Timestamp should not be blank")
            println("Location timestamp for $vin: $timestamp")
        } else {
            println("Location timestamp not available in response for $vin")
        }
    }
}
