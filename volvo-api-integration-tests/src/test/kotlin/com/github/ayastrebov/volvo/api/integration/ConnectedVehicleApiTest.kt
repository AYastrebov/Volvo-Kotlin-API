package com.github.ayastrebov.volvo.api.integration

import com.github.ayastrebov.volvo.api.integration.util.SharedTestContext
import com.github.ayastrebov.volvo.api.model.connectedvehicle.EngineStartRequest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the Connected Vehicle API.
 *
 * These tests call the real Volvo API and require valid credentials.
 * Tests are parameterized to run for ALL vehicles in the user's account.
 *
 * Tests are organized into categories:
 * - Vehicle Information: Basic vehicle data
 * - Status Data: Read-only vehicle status endpoints
 * - Commands: Remote command capabilities (read-only checks)
 * - Command Invocations: Actual command execution (tagged as "destructive")
 *
 * Tests will be skipped if the user doesn't have the required permissions.
 */
@DisplayName("Connected Vehicle API Integration Tests")
class ConnectedVehicleApiTest : BaseIntegrationTest() {

    // ==================== Vehicle Information ====================

    @Nested
    @DisplayName("Vehicle Information")
    inner class VehicleInformation {

        @Test
        @DisplayName("Get vehicle list returns user's vehicles")
        fun getVehicleList_returnsVehicles() = runTest {
            val response = runOrSkipOnPermissionDenied("Connected Vehicle API") {
                connectedVehicleClient.getVehicleList()
            }

            println("[getVehicleList] Response: $response")
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Vehicle list should not be null")
            assertTrue(response.data!!.isNotEmpty(), "Vehicle list should not be empty")

            println("Found ${response.data!!.size} vehicle(s):")
            response.data!!.forEach { vehicle ->
                println("  - VIN: ${vehicle.vin}")
            }
        }

        @ParameterizedTest(name = "Get vehicle details for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get vehicle details returns vehicle information")
        fun getVehicleDetails_returnsDetails(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Connected Vehicle API") {
                connectedVehicleClient.getVehicleDetails(vin)
            }

            logResponse("getVehicleDetails", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Vehicle details should not be null")
            assertEquals(vin, response.data!!.vin, "VIN should match")
        }
    }

    // ==================== Status Data ====================

    @Nested
    @DisplayName("Status Data")
    inner class StatusData {

        @ParameterizedTest(name = "Get window status for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get window status returns window states")
        fun getWindowStatus_returnsStatus(vin: String) = runTest {
            val response = connectedVehicleClient.getWindowStatus(vin)

            logResponse("getWindowStatus", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Window status should not be null")
        }

        @ParameterizedTest(name = "Get door and lock status for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get door and lock status returns door states")
        fun getDoorAndLockStatus_returnsStatus(vin: String) = runTest {
            val response = connectedVehicleClient.getDoorAndLockStatus(vin)

            logResponse("getDoorAndLockStatus", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Door and lock status should not be null")
        }

        @ParameterizedTest(name = "Get diagnostics for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get diagnostics returns diagnostic data")
        fun getDiagnostics_returnsData(vin: String) = runTest {
            val response = connectedVehicleClient.getDiagnostics(vin)

            logResponse("getDiagnostics", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Diagnostics should not be null")
        }

        @ParameterizedTest(name = "Get warnings for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get warnings returns vehicle warnings")
        fun getWarnings_returnsWarnings(vin: String) = runTest {
            val response = connectedVehicleClient.getWarnings(vin)

            logResponse("getWarnings", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Warnings should not be null")
        }

        @ParameterizedTest(name = "Get tyre status for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get tyre status returns tyre pressure data")
        fun getTyreStatus_returnsStatus(vin: String) = runTest {
            val response = connectedVehicleClient.getTyreStatus(vin)

            logResponse("getTyreStatus", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Tyre status should not be null")
        }

        @ParameterizedTest(name = "Get engine status for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get engine status returns running state")
        fun getEngineStatus_returnsStatus(vin: String) = runTest {
            val response = connectedVehicleClient.getEngineStatus(vin)

            logResponse("getEngineStatus", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine status should not be null")
        }

        @ParameterizedTest(name = "Get engine diagnostics for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get engine diagnostics returns diagnostic data")
        fun getEngineDiagnostics_returnsDiagnostics(vin: String) = runTest {
            val response = connectedVehicleClient.getEngineDiagnostics(vin)

            logResponse("getEngineDiagnostics", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine diagnostics should not be null")
        }

        @ParameterizedTest(name = "Get fuel amount for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get fuel amount returns fuel level")
        fun getFuelAmount_returnsAmount(vin: String) = runTest {
            val response = connectedVehicleClient.getFuelAmount(vin)

            logResponse("getFuelAmount", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Fuel amount should not be null")
        }

        @ParameterizedTest(name = "Get odometer for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get odometer returns mileage")
        fun getOdometer_returnsMileage(vin: String) = runTest {
            val response = connectedVehicleClient.getOdometer(vin)

            logResponse("getOdometer", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Odometer should not be null")
        }

        @ParameterizedTest(name = "Get statistics for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get statistics returns vehicle statistics")
        fun getStatistics_returnsStats(vin: String) = runTest {
            val response = connectedVehicleClient.getStatistics(vin)

            logResponse("getStatistics", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Statistics should not be null")
        }

        @ParameterizedTest(name = "Get brake status for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get brake status returns brake state")
        fun getBrakeStatus_returnsStatus(vin: String) = runTest {
            val response = connectedVehicleClient.getBrakeStatus(vin)

            logResponse("getBrakeStatus", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Brake status should not be null")
        }
    }

    // ==================== Commands (Read-Only) ====================

    @Nested
    @DisplayName("Commands")
    inner class Commands {

        @ParameterizedTest(name = "Get command list for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get command list returns available commands")
        fun getCommandList_returnsCommands(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Commands API") {
                connectedVehicleClient.getCommandList(vin)
            }

            logResponse("getCommandList", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Command list should not be null")
        }

        @ParameterizedTest(name = "Get command accessibility for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @DisplayName("Get command accessibility returns accessibility status")
        fun getCommandAccessibility_returnsAccessibility(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Commands API") {
                connectedVehicleClient.getCommandAccessibility(vin)
            }

            logResponse("getCommandAccessibility", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Command accessibility should not be null")
        }
    }

    // ==================== Command Invocations ====================

    @Nested
    @DisplayName("Command Invocations")
    @Tag("destructive")
    inner class CommandInvocations {

        @ParameterizedTest(name = "Invoke lock for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke lock command sends lock request")
        fun invokeLock_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Lock command") {
                connectedVehicleClient.invokeLock(vin)
            }

            logResponse("invokeLock", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Lock response should not be null")
        }

        @ParameterizedTest(name = "Invoke unlock for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke unlock command sends unlock request")
        fun invokeUnlock_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Unlock command") {
                connectedVehicleClient.invokeUnlock(vin)
            }

            logResponse("invokeUnlock", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Unlock response should not be null")
        }

        @ParameterizedTest(name = "Invoke honk for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke honk command sends honk request")
        fun invokeHonk_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Honk command") {
                connectedVehicleClient.invokeHonk(vin)
            }

            logResponse("invokeHonk", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Honk response should not be null")
        }

        @ParameterizedTest(name = "Invoke flash for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke flash command sends flash request")
        fun invokeFlash_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Flash command") {
                connectedVehicleClient.invokeFlash(vin)
            }

            logResponse("invokeFlash", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Flash response should not be null")
        }

        @ParameterizedTest(name = "Invoke honk and flash for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke honk and flash command sends combined request")
        fun invokeHonkFlash_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Honk and Flash command") {
                connectedVehicleClient.invokeHonkFlash(vin)
            }

            logResponse("invokeHonkFlash", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Honk and flash response should not be null")
        }

        @ParameterizedTest(name = "Invoke climatization start for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke climatization start command sends start request")
        fun invokeClimatizationStart_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Climatization command") {
                connectedVehicleClient.invokeClimatizationStart(vin)
            }

            logResponse("invokeClimatizationStart", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Climatization start response should not be null")
        }

        @ParameterizedTest(name = "Invoke climatization stop for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke climatization stop command sends stop request")
        fun invokeClimatizationStop_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Climatization command") {
                connectedVehicleClient.invokeClimatizationStop(vin)
            }

            logResponse("invokeClimatizationStop", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Climatization stop response should not be null")
        }

        @ParameterizedTest(name = "Invoke engine start for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke engine start command sends start request")
        fun invokeEngineStart_sendsCommand(vin: String) = runTest {
            val request = EngineStartRequest(runtimeMinutes = 5)
            val response = runOrSkipOnPermissionDenied("Engine command") {
                connectedVehicleClient.invokeEngineStart(vin, request)
            }

            logResponse("invokeEngineStart", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine start response should not be null")
        }

        @ParameterizedTest(name = "Invoke engine stop for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke engine stop command sends stop request")
        fun invokeEngineStop_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Engine command") {
                connectedVehicleClient.invokeEngineStop(vin)
            }

            logResponse("invokeEngineStop", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine stop response should not be null")
        }

        @ParameterizedTest(name = "Invoke lock with reduced guard for VIN {0}")
        @MethodSource("com.github.ayastrebov.volvo.api.integration.BaseIntegrationTest#allVins")
        @Tag("destructive")
        @DisplayName("Invoke lock with reduced guard command sends request")
        fun invokeLockReducedGuard_sendsCommand(vin: String) = runTest {
            val response = runOrSkipOnPermissionDenied("Lock command") {
                connectedVehicleClient.invokeLockReducedGuard(vin)
            }

            logResponse("invokeLockReducedGuard", vin, response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Lock reduced guard response should not be null")
        }
    }
}
