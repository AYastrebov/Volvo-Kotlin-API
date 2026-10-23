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
 * Tests for Energy API endpoints.
 *
 * Energy API v2 returns the payload directly, without the status/operationId/data envelope
 * used by the Connected Vehicle and Location APIs.
 */
class EnergyApiTest {

    // ==================== Capabilities ====================

    @Test
    fun getCapabilities_returnsVehicleCapabilities() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.capabilitiesResponse)

        val capabilities = client.getCapabilities(TestData.TEST_VIN)

        val energyState = capabilities.getEnergyState
        assertNotNull(energyState)
        assertEquals(true, energyState.isSupported)
        assertEquals(true, energyState.batteryChargeLevel?.isSupported)
        assertEquals(true, energyState.electricRange?.isSupported)
        assertEquals(false, energyState.chargingPower?.isSupported)
    }

    @Test
    fun getCapabilities_withRequestOptions_makesRequestSuccessfully() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.capabilitiesResponse)
        val options = RequestOptions(
            headers = mapOf("X-Custom-Header" to "test-value")
        )

        val capabilities = client.getCapabilities(TestData.TEST_VIN, options)

        assertNotNull(capabilities.getEnergyState)
    }

    // ==================== Energy State ====================

    @Test
    fun getEnergyState_returnsEnergyStateData() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.energyStateResponse)

        val state = client.getEnergyState(TestData.TEST_VIN)

        // Battery charge level
        assertNotNull(state.batteryChargeLevel)
        assertEquals("OK", state.batteryChargeLevel?.status)
        assertEquals(78.5f, state.batteryChargeLevel?.value)
        assertEquals("percent", state.batteryChargeLevel?.unit)

        // Electric range
        assertNotNull(state.electricRange)
        assertEquals("OK", state.electricRange?.status)
        assertEquals(285, state.electricRange?.value)
        assertEquals("km", state.electricRange?.unit)

        // Charger connection status
        assertNotNull(state.chargerConnectionStatus)
        assertEquals("OK", state.chargerConnectionStatus?.status)
        assertEquals("CONNECTED", state.chargerConnectionStatus?.value)

        // Charging status
        assertNotNull(state.chargingStatus)
        assertEquals("OK", state.chargingStatus?.status)
        assertEquals("CHARGING", state.chargingStatus?.value)

        // Charging type
        assertNotNull(state.chargingType)
        assertEquals("AC", state.chargingType?.value)

        // Estimated charging time
        assertNotNull(state.estimatedChargingTimeToTargetBatteryChargeLevel)
        assertEquals(120, state.estimatedChargingTimeToTargetBatteryChargeLevel?.value)
        assertEquals("minutes", state.estimatedChargingTimeToTargetBatteryChargeLevel?.unit)

        // Target battery charge level
        assertNotNull(state.targetBatteryChargeLevel)
        assertEquals(80, state.targetBatteryChargeLevel?.value)

        // Charging current limit
        assertNotNull(state.chargingCurrentLimit)
        assertEquals(16, state.chargingCurrentLimit?.value)
        assertEquals("ampere", state.chargingCurrentLimit?.unit)
    }

    @Test
    fun getEnergyState_withErrorStatus_handlesErrorGracefully() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.energyStateErrorResponse)

        val state = client.getEnergyState(TestData.TEST_VIN)

        // Battery charge level should have ERROR status
        assertNotNull(state.batteryChargeLevel)
        assertEquals("ERROR", state.batteryChargeLevel?.status)
        assertEquals("DATA_NOT_AVAILABLE", state.batteryChargeLevel?.code)
        assertEquals("Data is temporarily unavailable", state.batteryChargeLevel?.message)
        assertNull(state.batteryChargeLevel?.value)

        // Electric range should have OK status
        assertNotNull(state.electricRange)
        assertEquals("OK", state.electricRange?.status)
        assertEquals(285, state.electricRange?.value)
    }

    @Test
    fun getEnergyState_withRequestOptions_makesRequestSuccessfully() = runTest {
        val client = createTestClientWithResponse(EnergyFixtures.energyStateResponse)
        val options = RequestOptions(
            headers = mapOf("X-Custom-Header" to "test-value")
        )

        val state = client.getEnergyState(TestData.TEST_VIN, options)

        assertNotNull(state.batteryChargeLevel)
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
