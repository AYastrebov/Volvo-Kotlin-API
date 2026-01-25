package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.test.*
import com.github.ayastrebov.volvo.api.core.RequestOptions
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for Energy API endpoints.
 */
class EnergyApiTest {

    // ==================== Capabilities ====================

    @Test
    fun getCapabilities_returnsVehicleCapabilities() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.capabilitiesResponse)

        val response = client.getCapabilities(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertEquals(TestData.TEST_OPERATION_ID, response.operationId)
        assertNotNull(response.data)
        assertNotNull(response.data?.getEnergyState)
        assertEquals(true, response.data?.getEnergyState?.isSupported)
        assertEquals(true, response.data?.getEnergyState?.batteryChargeLevel?.isSupported)
        assertEquals(true, response.data?.getEnergyState?.electricRange?.isSupported)
        assertEquals(false, response.data?.getEnergyState?.chargingPower?.isSupported)
    }

    @Test
    fun getCapabilities_withRequestOptions_makesRequestSuccessfully() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.capabilitiesResponse)
        val options = RequestOptions(
            headers = mapOf("X-Custom-Header" to "test-value")
        )

        val response = client.getCapabilities(TestData.TEST_VIN, options)

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    // ==================== Energy State ====================

    @Test
    fun getEnergyState_returnsEnergyStateData() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.energyStateResponse)

        val response = client.getEnergyState(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertEquals(TestData.TEST_OPERATION_ID, response.operationId)
        assertNotNull(response.data)

        // Battery charge level
        assertNotNull(response.data?.batteryChargeLevel)
        assertEquals("OK", response.data?.batteryChargeLevel?.status)
        assertEquals(78.5f, response.data?.batteryChargeLevel?.value)
        assertEquals("percent", response.data?.batteryChargeLevel?.unit)

        // Electric range
        assertNotNull(response.data?.electricRange)
        assertEquals("OK", response.data?.electricRange?.status)
        assertEquals(285, response.data?.electricRange?.value)
        assertEquals("km", response.data?.electricRange?.unit)

        // Charger connection status
        assertNotNull(response.data?.chargerConnectionStatus)
        assertEquals("OK", response.data?.chargerConnectionStatus?.status)
        assertEquals("CONNECTED", response.data?.chargerConnectionStatus?.value)

        // Charging status
        assertNotNull(response.data?.chargingStatus)
        assertEquals("OK", response.data?.chargingStatus?.status)
        assertEquals("CHARGING", response.data?.chargingStatus?.value)

        // Charging type
        assertNotNull(response.data?.chargingType)
        assertEquals("AC", response.data?.chargingType?.value)

        // Estimated charging time
        assertNotNull(response.data?.estimatedChargingTimeToTargetBatteryChargeLevel)
        assertEquals(120, response.data?.estimatedChargingTimeToTargetBatteryChargeLevel?.value)
        assertEquals("minutes", response.data?.estimatedChargingTimeToTargetBatteryChargeLevel?.unit)

        // Target battery charge level
        assertNotNull(response.data?.targetBatteryChargeLevel)
        assertEquals(80, response.data?.targetBatteryChargeLevel?.value)

        // Charging current limit
        assertNotNull(response.data?.chargingCurrentLimit)
        assertEquals(16, response.data?.chargingCurrentLimit?.value)
        assertEquals("ampere", response.data?.chargingCurrentLimit?.unit)
    }

    @Test
    fun getEnergyState_withErrorStatus_handlesErrorGracefully() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.energyStateErrorResponse)

        val response = client.getEnergyState(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)

        // Battery charge level should have ERROR status
        assertNotNull(response.data?.batteryChargeLevel)
        assertEquals("ERROR", response.data?.batteryChargeLevel?.status)
        assertEquals("DATA_NOT_AVAILABLE", response.data?.batteryChargeLevel?.code)
        assertEquals("Data is temporarily unavailable", response.data?.batteryChargeLevel?.message)

        // Electric range should have OK status
        assertNotNull(response.data?.electricRange)
        assertEquals("OK", response.data?.electricRange?.status)
        assertEquals(285, response.data?.electricRange?.value)
    }

    @Test
    fun getEnergyState_withRequestOptions_makesRequestSuccessfully() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.energyStateResponse)
        val options = RequestOptions(
            headers = mapOf("X-Custom-Header" to "test-value")
        )

        val response = client.getEnergyState(TestData.TEST_VIN, options)

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    // ==================== Request Verification ====================

    @Test
    fun getCapabilities_makesCorrectRequest() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, EnergyFixtures.capabilitiesResponse)
        )
        val client = createTestClient(capturingEngine.engine)

        client.getCapabilities(TestData.TEST_VIN)

        assertEquals(1, capturingEngine.requests.size)
        val request = capturingEngine.requests.first()
        assertTrue(request.url.encodedPath.contains("energy/v2/vehicles/${TestData.TEST_VIN}/capabilities"))
        assertEquals(HttpMethod.Get, request.method)
    }

    @Test
    fun getEnergyState_makesCorrectRequest() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, EnergyFixtures.energyStateResponse)
        )
        val client = createTestClient(capturingEngine.engine)

        client.getEnergyState(TestData.TEST_VIN)

        assertEquals(1, capturingEngine.requests.size)
        val request = capturingEngine.requests.first()
        assertTrue(request.url.encodedPath.contains("energy/v2/vehicles/${TestData.TEST_VIN}/state"))
        assertEquals(HttpMethod.Get, request.method)
    }
}
