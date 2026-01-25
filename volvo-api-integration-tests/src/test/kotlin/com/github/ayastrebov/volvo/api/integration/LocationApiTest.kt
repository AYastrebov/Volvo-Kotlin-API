package com.github.ayastrebov.volvo.api.integration

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the Location API.
 *
 * These tests call the real Volvo API and require valid credentials.
 * Tests will be skipped if the user doesn't have Location API access.
 */
@DisplayName("Location API Integration Tests")
class LocationApiTest : BaseIntegrationTest() {

    @Test
    @DisplayName("Get vehicle location returns GeoJSON Feature")
    fun getVehicleLocation_returnsLocation() = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            client.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data, "Location response should not be null")
        assertEquals("Feature", response.data!!.type, "Response should be a GeoJSON Feature")
    }

    @Test
    @DisplayName("Location response contains geometry")
    fun locationResponse_containsGeometry() = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            client.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data?.geometry, "Geometry should not be null")
        assertEquals("Point", response.data!!.geometry!!.type, "Geometry type should be Point")
    }

    @Test
    @DisplayName("Location coordinates are valid")
    fun locationCoordinates_areValid() = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            client.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", response)
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

        println("Vehicle location: ($latitude, $longitude)")
    }

    @Test
    @DisplayName("Location response contains properties")
    fun locationResponse_containsProperties() = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            client.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data?.properties, "Properties should not be null")

        // Log available properties for debugging
        // Properties is a Map<String, String>
        response.data?.properties?.let { props ->
            println("Location properties:")
            props.forEach { (key, value) ->
                println("  - $key: $value")
            }
        }
    }

    @Test
    @DisplayName("Location heading is within valid range if present")
    fun locationHeading_isWithinValidRange() = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            client.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", response)
        assertSuccessStatus(response.status)

        // Properties is a Map<String, String>, so we need to parse heading as Double
        response.data?.properties?.get("heading")?.toDoubleOrNull()?.let { heading ->
            assertTrue(
                heading >= 0.0 && heading < 360.0,
                "Heading should be between 0 and 360 degrees, but was $heading"
            )
            println("Vehicle heading: $heading degrees")
        }
    }

    @Test
    @DisplayName("Location timestamp is present if available")
    fun locationTimestamp_isPresent() = runTest {
        val response = runOrSkipOnPermissionDenied("Location API") {
            client.getVehicleLocation(vin)
        }

        logResponse("getVehicleLocation", response)
        assertSuccessStatus(response.status)

        // Properties is a Map<String, String>
        val timestamp = response.data?.properties?.get("timestamp")
        if (timestamp != null) {
            assertTrue(timestamp.isNotBlank(), "Timestamp should not be blank")
            println("Location timestamp: $timestamp")
        } else {
            println("Location timestamp not available in response")
        }
    }
}
