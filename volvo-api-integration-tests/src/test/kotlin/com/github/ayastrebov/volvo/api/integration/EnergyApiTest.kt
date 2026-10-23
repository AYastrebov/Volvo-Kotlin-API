package com.github.ayastrebov.volvo.api.integration

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the Energy API.
 *
 * These tests call the real Volvo API and require valid credentials.
 * Tests are parameterized to run for ALL vehicles in the user's account.
 *
 * Note: Energy API endpoints are only available for electric and hybrid vehicles.
 * Tests are skipped when capabilities report that the energy state is not supported.
 */
@DisplayName("Energy API Integration Tests")
class EnergyApiTest : BaseIntegrationTest() {

    @ParameterizedTest(name = "Get energy capabilities for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Get energy capabilities returns supported features")
    fun getCapabilities_returnsCapabilities(vin: String) = runTest {
        val capabilities = runOrSkipOnPermissionDenied("Energy API") {
            energyClient.getCapabilities(vin)
        }

        logResponse("getCapabilities", vin, capabilities)
        // Energy v2 has no envelope: the payload itself must have decoded.
        assertNotNull(capabilities.getEnergyState, "Capabilities payload should contain getEnergyState")
    }

    @ParameterizedTest(name = "Get energy state for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Get energy state returns current battery/charging state")
    fun getEnergyState_returnsState(vin: String) = runTest {
        assumeEnergySupported(vin)
        val state = runOrSkipOnPermissionDenied("Energy API") {
            energyClient.getEnergyState(vin)
        }

        logResponse("getEnergyState", vin, state)
        assertNotNull(state.batteryChargeLevel, "Energy state should contain batteryChargeLevel")
        assertTrue(
            state.batteryChargeLevel?.status in setOf("OK", "ERROR"),
            "Resource status should be OK or ERROR, but was ${state.batteryChargeLevel?.status}"
        )
    }

    @ParameterizedTest(name = "Energy capabilities and state consistency for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Energy capabilities and state are consistent")
    fun capabilitiesAndState_areConsistent(vin: String) = runTest {
        val capabilities = runOrSkipOnPermissionDenied("Energy API") {
            energyClient.getCapabilities(vin)
        }
        assumeTrue(
            capabilities.getEnergyState?.isSupported == true,
            "Skipping: Vehicle $vin doesn't support Energy API (not electric/hybrid)"
        )

        val state = runOrSkipOnPermissionDenied("Energy API") {
            energyClient.getEnergyState(vin)
        }

        // Every resource the capabilities report as supported should be present in the state.
        if (capabilities.getEnergyState?.batteryChargeLevel?.isSupported == true) {
            assertNotNull(state.batteryChargeLevel, "batteryChargeLevel is supported but missing from state")
        }
        if (capabilities.getEnergyState?.electricRange?.isSupported == true) {
            assertNotNull(state.electricRange, "electricRange is supported but missing from state")
        }
        if (capabilities.getEnergyState?.chargerConnectionStatus?.isSupported == true) {
            assertNotNull(state.chargerConnectionStatus, "chargerConnectionStatus is supported but missing from state")
        }
    }

    @ParameterizedTest(name = "Battery charge level validation for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Battery charge level is within valid range")
    fun batteryChargeLevel_isWithinValidRange(vin: String) = runTest {
        assumeEnergySupported(vin)
        val state = runOrSkipOnPermissionDenied("Energy API") {
            energyClient.getEnergyState(vin)
        }

        logResponse("getEnergyState", vin, state)
        val resource = state.batteryChargeLevel
        assertNotNull(resource, "batteryChargeLevel should be present")
        if (resource.status == "OK") {
            val level = assertNotNull(resource.value, "OK resource should carry a value")
            assertTrue(level >= 0f, "Battery level should be >= 0, but was $level")
            assertTrue(level <= 100f, "Battery level should be <= 100, but was $level")
            println("Battery charge level for $vin: $level ${resource.unit}")
        } else {
            assertEquals("ERROR", resource.status)
            assertNotNull(resource.code, "ERROR resource should carry a code")
        }
    }

    @ParameterizedTest(name = "Electric range validation for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Electric range is non-negative")
    fun electricRange_isNonNegative(vin: String) = runTest {
        assumeEnergySupported(vin)
        val state = runOrSkipOnPermissionDenied("Energy API") {
            energyClient.getEnergyState(vin)
        }

        logResponse("getEnergyState", vin, state)
        state.electricRange?.value?.let { range ->
            assertTrue(range >= 0, "Electric range should be >= 0, but was $range")
            println("Electric range for $vin: $range ${state.electricRange?.unit}")
        }
    }

    private suspend fun assumeEnergySupported(vin: String) {
        val capabilities = runOrSkipOnPermissionDenied("Energy API") {
            energyClient.getCapabilities(vin)
        }
        assumeTrue(
            capabilities.getEnergyState?.isSupported == true,
            "Skipping: Vehicle $vin doesn't support Energy API (not electric/hybrid)"
        )
    }
}
