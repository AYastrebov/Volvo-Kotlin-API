package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.test.*
import com.github.ayastrebov.volvo.api.core.RequestOptions
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Tests for Location API endpoints.
 */
class LocationApiTest {

    // ==================== Location ====================

    @Test
    fun getVehicleLocation_returnsGeoJsonFeature() = runTest {
        val client = createTestClientWithResponse(LocationFixtures.locationResponse)

        val response = client.getVehicleLocation(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertEquals(TestData.TEST_OPERATION_ID, response.operationId)
        assertNotNull(response.data)

        // GeoJSON Feature
        assertEquals("Feature", response.data?.type)
        assertNotNull(response.data?.properties)
        assertEquals("125", response.data?.properties?.get("heading"))

        // GeoJSON Point geometry
        assertNotNull(response.data?.geometry)
        assertEquals("Point", response.data?.geometry?.type)
        assertNotNull(response.data?.geometry?.coordinates)
        assertEquals(3, response.data?.geometry?.coordinates?.size)
    }

    @Test
    fun getVehicleLocation_coordinatesAreParsedCorrectly() = runTest {
        val client = createTestClientWithResponse(LocationFixtures.locationResponse)

        val response = client.getVehicleLocation(TestData.TEST_VIN)

        val point = response.data?.geometry
        assertNotNull(point)

        // Coordinates: [longitude, latitude, altitude]
        assertEquals(18.0686, point.longitude)
        assertEquals(59.3293, point.latitude)
        assertEquals(25.5, point.altitude)
    }

    @Test
    fun getVehicleLocation_withoutAltitude_handlesOptionalField() = runTest {
        val client = createTestClientWithResponse(LocationFixtures.locationResponseWithoutAltitude)

        val response = client.getVehicleLocation(TestData.TEST_VIN)

        val point = response.data?.geometry
        assertNotNull(point)

        // Only longitude and latitude
        assertEquals(18.0686, point.longitude)
        assertEquals(59.3293, point.latitude)
        assertNull(point.altitude)
    }

    @Test
    fun getVehicleLocation_withEmptyProperties_handlesGracefully() = runTest {
        val client = createTestClientWithResponse(LocationFixtures.locationResponseWithoutAltitude)

        val response = client.getVehicleLocation(TestData.TEST_VIN)

        assertNotNull(response.data?.properties)
        assertTrue(response.data?.properties?.isEmpty() == true)
    }

    @Test
    fun getVehicleLocation_withRequestOptions_makesRequestSuccessfully() = runTest {
        val client = createTestClientWithResponse(LocationFixtures.locationResponse)
        val options = RequestOptions(
            headers = mapOf("X-Custom-Header" to "test-value")
        )

        val response = client.getVehicleLocation(TestData.TEST_VIN, options)

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    // ==================== Request Verification ====================

    @Test
    fun getVehicleLocation_makesCorrectRequest() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, LocationFixtures.locationResponse)
        )
        val client = createTestClient(capturingEngine.engine)

        client.getVehicleLocation(TestData.TEST_VIN)

        assertEquals(1, capturingEngine.requests.size)
        val request = capturingEngine.requests.first()
        assertTrue(request.url.encodedPath.contains("location/v1/vehicles/${TestData.TEST_VIN}/location"))
        assertEquals(HttpMethod.Get, request.method)
    }
}
