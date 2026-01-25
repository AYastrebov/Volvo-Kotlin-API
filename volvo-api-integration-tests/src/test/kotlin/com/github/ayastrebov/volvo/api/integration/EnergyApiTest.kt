package com.github.ayastrebov.volvo.api.integration

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the Energy API.
 *
 * These tests call the real Volvo API and require valid credentials.
 * Tests are parameterized to run for ALL vehicles in the user's account.
 *
 * Note: Energy API endpoints are only available for electric and hybrid vehicles.
 * Tests will be skipped if the user doesn't have Energy API access.
 */
@DisplayName("Energy API Integration Tests")
class EnergyApiTest : BaseIntegrationTest() {

    @ParameterizedTest(name = "Get energy capabilities for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Get energy capabilities returns supported features")
    fun getCapabilities_returnsCapabilities(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Energy API") {
            client.getCapabilities(vin)
        }

        logResponse("getCapabilities", vin, response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data, "Capabilities should not be null")
    }

    @ParameterizedTest(name = "Get energy state for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Get energy state returns current battery/charging state")
    fun getEnergyState_returnsState(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Energy API") {
            client.getEnergyState(vin)
        }

        logResponse("getEnergyState", vin, response)
        assertSuccessStatus(response.status)
        assertNotNull(response.data, "Energy state should not be null")
    }

    @ParameterizedTest(name = "Energy capabilities and state consistency for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Energy capabilities and state are consistent")
    fun capabilitiesAndState_areConsistent(vin: String) = runTest {
        val capabilities = runOrSkipOnPermissionDenied("Energy API") {
            client.getCapabilities(vin)
        }
        val state = runOrSkipOnPermissionDenied("Energy API") {
            client.getEnergyState(vin)
        }

        assertSuccessStatus(capabilities.status)
        assertSuccessStatus(state.status)

        // If capabilities say getEnergyState is supported, we should get data
        if (capabilities.data?.getEnergyState?.isSupported == true) {
            assertNotNull(state.data, "Energy state should be available when capability is supported")
        }
    }

    @ParameterizedTest(name = "Battery charge level validation for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Battery charge level is within valid range")
    fun batteryChargeLevel_isWithinValidRange(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Energy API") {
            client.getEnergyState(vin)
        }

        logResponse("getEnergyState", vin, response)
        assertSuccessStatus(response.status)

        response.data?.batteryChargeLevel?.value?.let { level ->
            assertTrue(level >= 0f, "Battery level should be >= 0, but was $level")
            assertTrue(level <= 100f, "Battery level should be <= 100, but was $level")
            println("Battery charge level for $vin: $level%")
        }
    }

    @ParameterizedTest(name = "Electric range validation for VIN {0}")
    @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
    @DisplayName("Electric range is non-negative")
    fun electricRange_isNonNegative(vin: String) = runTest {
        val response = runOrSkipOnPermissionDenied("Energy API") {
            client.getEnergyState(vin)
        }

        logResponse("getEnergyState", vin, response)
        assertSuccessStatus(response.status)

        response.data?.electricRange?.value?.let { range ->
            assertTrue(range >= 0, "Electric range should be >= 0, but was $range")
            println("Electric range for $vin: $range ${response.data?.electricRange?.unit}")
        }
    }
}
