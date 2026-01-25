package com.github.ayastrebov.volvo.api.integration

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the Energy API.
 *
 * These tests call the real Volvo API and require valid credentials.
 * Note: Energy API endpoints are only available for electric and hybrid vehicles.
 */
@DisplayName("Energy API Integration Tests")
class EnergyApiTest : BaseIntegrationTest() {

    @Test
    @DisplayName("Get energy capabilities returns supported features")
    fun getCapabilities_returnsCapabilities() = runTest {
        val response = client.getCapabilities(vin)

        logResponse("getCapabilities", response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data, "Capabilities should not be null")

        // Log what's supported for debugging
        val capabilities = response.data!!
        println("Energy capabilities:")
        println("  - getEnergyState supported: ${capabilities.getEnergyState?.isSupported}")
        capabilities.getEnergyState?.let { energyState ->
            println("  - batteryChargeLevel: ${energyState.batteryChargeLevel?.isSupported}")
            println("  - electricRange: ${energyState.electricRange?.isSupported}")
            println("  - estimatedChargingTimeToTargetBatteryChargeLevel: ${energyState.estimatedChargingTimeToTargetBatteryChargeLevel?.isSupported}")
            println("  - chargerConnectionStatus: ${energyState.chargerConnectionStatus?.isSupported}")
            println("  - chargingSystemStatus: ${energyState.chargingSystemStatus?.isSupported}")
        }
    }

    @Test
    @DisplayName("Get energy state returns current battery/charging state")
    fun getEnergyState_returnsState() = runTest {
        val response = client.getEnergyState(vin)

        logResponse("getEnergyState", response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data, "Energy state should not be null")

        // Log actual values for debugging
        val energyState = response.data!!
        println("Energy state:")
        energyState.batteryChargeLevel?.let {
            println("  - Battery charge level: ${it.value}% (status: ${it.status})")
        }
        energyState.electricRange?.let {
            println("  - Electric range: ${it.value} ${it.unit} (status: ${it.status})")
        }
        energyState.estimatedChargingTimeToTargetBatteryChargeLevel?.let {
            println("  - Estimated charging time: ${it.value} ${it.unit} (status: ${it.status})")
        }
        energyState.chargerConnectionStatus?.let {
            println("  - Charger connection: ${it.value} (status: ${it.status})")
        }
        energyState.chargingStatus?.let {
            println("  - Charging status: ${it.value} (status: ${it.status})")
        }
    }

    @Test
    @DisplayName("Energy capabilities and state are consistent")
    fun capabilitiesAndState_areConsistent() = runTest {
        val capabilities = client.getCapabilities(vin)
        val state = client.getEnergyState(vin)

        assertSuccessStatus(capabilities.status)
        assertSuccessStatus(state.status)

        // If capabilities say getEnergyState is supported, we should get data
        if (capabilities.data?.getEnergyState?.isSupported == true) {
            assertNotNull(state.data, "Energy state should be available when capability is supported")
        }

        // If battery charge level is supported, we should have it in state
        if (capabilities.data?.getEnergyState?.batteryChargeLevel?.isSupported == true) {
            assertNotNull(
                state.data?.batteryChargeLevel,
                "Battery charge level should be in state when supported"
            )
        }

        // If electric range is supported, we should have it in state
        if (capabilities.data?.getEnergyState?.electricRange?.isSupported == true) {
            assertNotNull(
                state.data?.electricRange,
                "Electric range should be in state when supported"
            )
        }
    }

    @Test
    @DisplayName("Battery charge level is within valid range")
    fun batteryChargeLevel_isWithinValidRange() = runTest {
        val response = client.getEnergyState(vin)

        logResponse("getEnergyState", response)
        assertSuccessStatus(response.status)

        response.data?.batteryChargeLevel?.value?.let { level ->
            assertTrue(level >= 0f, "Battery level should be >= 0, but was $level")
            assertTrue(level <= 100f, "Battery level should be <= 100, but was $level")
            println("Battery charge level: $level%")
        }
    }

    @Test
    @DisplayName("Electric range is non-negative")
    fun electricRange_isNonNegative() = runTest {
        val response = client.getEnergyState(vin)

        logResponse("getEnergyState", response)
        assertSuccessStatus(response.status)

        response.data?.electricRange?.value?.let { range ->
            assertTrue(range >= 0, "Electric range should be >= 0, but was $range")
            println("Electric range: $range ${response.data?.electricRange?.unit}")
        }
    }
}
