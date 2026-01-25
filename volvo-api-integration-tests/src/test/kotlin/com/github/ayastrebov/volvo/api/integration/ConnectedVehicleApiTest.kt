package com.github.ayastrebov.volvo.api.integration

import com.github.ayastrebov.volvo.api.model.connectedvehicle.EngineStartRequest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for the Connected Vehicle API.
 *
 * These tests call the real Volvo API and require valid credentials.
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
            val response = client.getVehicleList()

            logResponse("getVehicleList", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Vehicle list data should not be null")
            assertTrue(response.data!!.isNotEmpty(), "User should have at least one vehicle")

            // Verify our test VIN is in the list
            val vinList = response.data!!.map { it.vin }
            assertTrue(vin in vinList, "Test VIN should be in the vehicle list")
        }

        @Test
        @DisplayName("Get vehicle details returns vehicle information")
        fun getVehicleDetails_returnsDetails() = runTest {
            val response = client.getVehicleDetails(vin)

            logResponse("getVehicleDetails", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Vehicle details should not be null")
            assertEquals(vin, response.data!!.vin, "VIN should match")
            assertNotNull(response.data!!.modelYear, "Model year should be present")
        }
    }

    // ==================== Status Data ====================

    @Nested
    @DisplayName("Status Data")
    inner class StatusData {

        @Test
        @DisplayName("Get window status returns window states")
        fun getWindowStatus_returnsStatus() = runTest {
            val response = client.getWindowStatus(vin)

            logResponse("getWindowStatus", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Window status should not be null")
        }

        @Test
        @DisplayName("Get door and lock status returns door states")
        fun getDoorAndLockStatus_returnsStatus() = runTest {
            val response = client.getDoorAndLockStatus(vin)

            logResponse("getDoorAndLockStatus", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Door and lock status should not be null")
        }

        @Test
        @DisplayName("Get diagnostics returns diagnostic data")
        fun getDiagnostics_returnsData() = runTest {
            val response = client.getDiagnostics(vin)

            logResponse("getDiagnostics", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Diagnostics should not be null")
        }

        @Test
        @DisplayName("Get warnings returns vehicle warnings")
        fun getWarnings_returnsWarnings() = runTest {
            val response = client.getWarnings(vin)

            logResponse("getWarnings", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Warnings should not be null")
        }

        @Test
        @DisplayName("Get tyre status returns tyre pressure data")
        fun getTyreStatus_returnsStatus() = runTest {
            val response = client.getTyreStatus(vin)

            logResponse("getTyreStatus", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Tyre status should not be null")
        }

        @Test
        @DisplayName("Get engine status returns running state")
        fun getEngineStatus_returnsStatus() = runTest {
            val response = client.getEngineStatus(vin)

            logResponse("getEngineStatus", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine status should not be null")
        }

        @Test
        @DisplayName("Get engine diagnostics returns diagnostic data")
        fun getEngineDiagnostics_returnsDiagnostics() = runTest {
            val response = client.getEngineDiagnostics(vin)

            logResponse("getEngineDiagnostics", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine diagnostics should not be null")
        }

        @Test
        @DisplayName("Get fuel amount returns fuel level")
        fun getFuelAmount_returnsAmount() = runTest {
            val response = client.getFuelAmount(vin)

            logResponse("getFuelAmount", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Fuel amount should not be null")
        }

        @Test
        @DisplayName("Get odometer returns mileage")
        fun getOdometer_returnsMileage() = runTest {
            val response = client.getOdometer(vin)

            logResponse("getOdometer", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Odometer should not be null")
        }

        @Test
        @DisplayName("Get statistics returns vehicle statistics")
        fun getStatistics_returnsStats() = runTest {
            val response = client.getStatistics(vin)

            logResponse("getStatistics", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Statistics should not be null")
        }

        @Test
        @DisplayName("Get brake status returns brake state")
        fun getBrakeStatus_returnsStatus() = runTest {
            val response = client.getBrakeStatus(vin)

            logResponse("getBrakeStatus", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Brake status should not be null")
        }
    }

    // ==================== Commands (Read-Only) ====================

    @Nested
    @DisplayName("Commands")
    inner class Commands {

        @Test
        @DisplayName("Get command list returns available commands")
        fun getCommandList_returnsCommands() = runTest {
            val response = runOrSkipOnPermissionDenied("Commands API") {
                client.getCommandList(vin)
            }

            logResponse("getCommandList", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Command list should not be null")
            assertTrue(response.data!!.isNotEmpty(), "Should have at least one command available")

            // Log available commands for debugging
            println("Available commands: ${response.data!!.mapNotNull { it.command }.joinToString()}")
        }

        @Test
        @DisplayName("Get command accessibility returns accessibility status")
        fun getCommandAccessibility_returnsAccessibility() = runTest {
            val response = runOrSkipOnPermissionDenied("Commands API") {
                client.getCommandAccessibility(vin)
            }

            logResponse("getCommandAccessibility", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Command accessibility should not be null")
            assertNotNull(response.data!!.availableCommands, "Available commands list should not be null")
        }
    }

    // ==================== Command Invocations ====================

    @Nested
    @DisplayName("Command Invocations")
    @Tag("destructive")
    inner class CommandInvocations {

        @Test
        @Tag("destructive")
        @DisplayName("Invoke lock command sends lock request")
        fun invokeLock_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Lock command") {
                client.invokeLock(vin)
            }

            logResponse("invokeLock", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Lock response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
            println("Lock invoke status: ${response.data!!.invokeStatus}")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke unlock command sends unlock request")
        fun invokeUnlock_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Unlock command") {
                client.invokeUnlock(vin)
            }

            logResponse("invokeUnlock", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Unlock response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
            println("Unlock invoke status: ${response.data!!.invokeStatus}")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke honk command sends honk request")
        fun invokeHonk_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Honk command") {
                client.invokeHonk(vin)
            }

            logResponse("invokeHonk", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Honk response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke flash command sends flash request")
        fun invokeFlash_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Flash command") {
                client.invokeFlash(vin)
            }

            logResponse("invokeFlash", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Flash response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke honk and flash command sends combined request")
        fun invokeHonkFlash_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Honk and Flash command") {
                client.invokeHonkFlash(vin)
            }

            logResponse("invokeHonkFlash", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Honk and flash response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke climatization start command sends start request")
        fun invokeClimatizationStart_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Climatization command") {
                client.invokeClimatizationStart(vin)
            }

            logResponse("invokeClimatizationStart", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Climatization start response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke climatization stop command sends stop request")
        fun invokeClimatizationStop_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Climatization command") {
                client.invokeClimatizationStop(vin)
            }

            logResponse("invokeClimatizationStop", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Climatization stop response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke engine start command sends start request")
        fun invokeEngineStart_sendsCommand() = runTest {
            val request = EngineStartRequest(runtimeMinutes = 5)
            val response = runOrSkipOnPermissionDenied("Engine command") {
                client.invokeEngineStart(vin, request)
            }

            logResponse("invokeEngineStart", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine start response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke engine stop command sends stop request")
        fun invokeEngineStop_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Engine command") {
                client.invokeEngineStop(vin)
            }

            logResponse("invokeEngineStop", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Engine stop response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }

        @Test
        @Tag("destructive")
        @DisplayName("Invoke lock with reduced guard command sends request")
        fun invokeLockReducedGuard_sendsCommand() = runTest {
            val response = runOrSkipOnPermissionDenied("Lock command") {
                client.invokeLockReducedGuard(vin)
            }

            logResponse("invokeLockReducedGuard", response)
            assertSuccessStatus(response.status)
            assertNotNull(response.data, "Lock reduced guard response should not be null")
            assertNotNull(response.data!!.invokeStatus, "Invoke status should not be null")
        }
    }
}
