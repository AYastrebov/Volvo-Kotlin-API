package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.test.*
import com.github.ayastrebov.volvo.api.model.connectedvehicle.EngineStartRequest
import com.github.ayastrebov.volvo.api.core.RequestOptions
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for Connected Vehicle API endpoints.
 */
class ConnectedVehicleApiTest {

    // ==================== Vehicle Information ====================

    @Test
    fun getVehicleList_returnsListOfVehicles() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.vehicleListResponse)

        val response = client.getVehicleList()

        assertEquals(200, response.status)
        assertEquals(TestData.TEST_OPERATION_ID, response.operationId)
        assertNotNull(response.data)
        assertEquals(2, response.data?.size)
        assertEquals(TestData.TEST_VIN, response.data?.first()?.vin)
    }

    @Test
    fun getVehicleDetails_returnsVehicleInformation() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.vehicleDetailsResponse)

        val response = client.getVehicleDetails(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertEquals(TestData.TEST_OPERATION_ID, response.operationId)
        assertNotNull(response.data)
        assertEquals(TestData.TEST_VIN, response.data?.vin)
        assertEquals(2024, response.data?.modelYear)
        assertEquals("AUTOMATIC", response.data?.gearbox)
        assertEquals("ELECTRIC", response.data?.fuelType)
        assertEquals("BLACK", response.data?.externalColour)
        assertEquals(78.0, response.data?.batteryCapacityKWH)
        assertNotNull(response.data?.images)
        assertEquals("https://example.com/exterior.jpg", response.data?.images?.exteriorImageUrl)
        assertNotNull(response.data?.descriptions)
        assertEquals("XC40 Recharge", response.data?.descriptions?.model)
    }

    // ==================== Status Data ====================

    @Test
    fun getWindowStatus_returnsWindowStatuses() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.windowStatusResponse)

        val response = client.getWindowStatus(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("CLOSED", response.data?.frontLeftWindow?.value)
        assertEquals("CLOSED", response.data?.frontRightWindow?.value)
        assertEquals("OPEN", response.data?.rearLeftWindow?.value)
        assertEquals("CLOSED", response.data?.rearRightWindow?.value)
        assertEquals("CLOSED", response.data?.sunroof?.value)
    }

    @Test
    fun getWarnings_returnsVehicleWarnings() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.warningsResponse)

        val response = client.getWarnings(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("NO_WARNING", response.data?.brakeLightCenterWarning?.value)
        assertEquals("NO_WARNING", response.data?.lowBeamLeftWarning?.value)
    }

    @Test
    fun getTyreStatus_returnsTyrePressureValues() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.tyreStatusResponse)

        val response = client.getTyreStatus(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("NO_WARNING", response.data?.frontLeft?.value)
        assertEquals("NO_WARNING", response.data?.frontRight?.value)
        assertEquals("NO_WARNING", response.data?.rearLeft?.value)
        assertEquals("NO_WARNING", response.data?.rearRight?.value)
    }

    @Test
    fun getStatistics_returnsVehicleStatistics() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.statisticsResponse)

        val response = client.getStatistics(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("5.2", response.data?.averageFuelConsumption?.value)
        assertEquals("45", response.data?.averageSpeed?.value)
        assertEquals("1250", response.data?.tripMeter1?.value)
        assertEquals("560", response.data?.tripMeter2?.value)
        assertEquals("450", response.data?.distanceToEmpty?.value)
    }

    @Test
    fun getOdometer_returnsOdometerValue() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.odometerResponse)

        val response = client.getOdometer(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals(52340, response.data?.odometer?.value)
        assertEquals("km", response.data?.odometer?.unit)
    }

    @Test
    fun getFuelAmount_returnsFuelLevel() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.fuelAmountResponse)

        val response = client.getFuelAmount(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals(45.5, response.data?.fuelAmount?.value)
        assertEquals("liters", response.data?.fuelAmount?.unit)
    }

    @Test
    fun getEngineDiagnostics_returnsEngineDiagnosticValues() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.engineDiagnosticsResponse)

        val response = client.getEngineDiagnostics(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("NO_WARNING", response.data?.engineCoolantLevelWarning?.value)
        assertEquals("NO_WARNING", response.data?.oilLevelWarning?.value)
    }

    @Test
    fun getEngineStatus_returnsEngineRunningStatus() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.engineStatusResponse)

        val response = client.getEngineStatus(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("STOPPED", response.data?.engineStatus?.value)
    }

    @Test
    fun getDoorAndLockStatus_returnsDoorAndLockStatuses() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.doorAndLockStatusResponse)

        val response = client.getDoorAndLockStatus(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("LOCKED", response.data?.centralLock?.value)
        assertEquals("CLOSED", response.data?.frontLeftDoor?.value)
        assertEquals("CLOSED", response.data?.frontRightDoor?.value)
        assertEquals("CLOSED", response.data?.hood?.value)
        assertEquals("CLOSED", response.data?.tailgate?.value)
    }

    @Test
    fun getDiagnostics_returnsDiagnosticValues() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.diagnosticsResponse)

        val response = client.getDiagnostics(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("NO_WARNING", response.data?.serviceWarning?.value)
        assertEquals("NO_WARNING", response.data?.washerFluidLevelWarning?.value)
        assertEquals(15000, response.data?.distanceToService?.value)
        assertEquals(180, response.data?.timeToService?.value)
    }

    @Test
    fun getBrakeStatus_returnsBrakeFluidLevel() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.brakeStatusResponse)

        val response = client.getBrakeStatus(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("NO_WARNING", response.data?.brakeFluidLevelWarning?.value)
    }

    // ==================== Commands ====================

    @Test
    fun getCommandList_returnsAvailableCommands() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandListResponse)

        val response = client.getCommandList(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertTrue(response.data!!.isNotEmpty())
        assertEquals("LOCK", response.data?.first()?.command)
    }

    @Test
    fun getCommandAccessibility_returnsCommandAccessibility() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandAccessibilityResponse)

        val response = client.getCommandAccessibility(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertNotNull(response.data?.availableCommands)
        assertEquals(2, response.data?.availableCommands?.size)
        assertNotNull(response.data?.unavailableCommands)
        assertEquals(1, response.data?.unavailableCommands?.size)
        assertEquals("NOT_SUPPORTED", response.data?.unavailableCommands?.first()?.reason)
    }

    // ==================== Command Invocations ====================

    @Test
    fun invokeUnlock_sendsUnlockCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.unlockCommandResponse)

        val response = client.invokeUnlock(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals(TestData.TEST_VIN, response.data?.vin)
        assertEquals("COMPLETED", response.data?.invokeStatus)
        assertEquals(true, response.data?.readyToUnlock)
    }

    @Test
    fun invokeLock_sendsLockCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeLock(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals(TestData.TEST_VIN, response.data?.vin)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeLockReducedGuard_sendsLockReducedGuardCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeLockReducedGuard(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeHonk_sendsHonkCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeHonk(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeFlash_sendsFlashCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeFlash(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeHonkFlash_sendsHonkFlashCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeHonkFlash(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeEngineStart_sendsEngineStartCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)
        val request = EngineStartRequest(runtimeMinutes = 10)

        val response = client.invokeEngineStart(TestData.TEST_VIN, request)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeEngineStop_sendsEngineStopCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeEngineStop(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeClimatizationStart_sendsClimatizationStartCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeClimatizationStart(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    @Test
    fun invokeClimatizationStop_sendsClimatizationStopCommand() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.commandResponse)

        val response = client.invokeClimatizationStop(TestData.TEST_VIN)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertEquals("COMPLETED", response.data?.invokeStatus)
    }

    // ==================== Request Options ====================

    @Test
    fun getVehicleList_withRequestOptions_makesRequestSuccessfully() = runTest {
        val client = createTestClientWithResponse(ConnectedVehicleFixtures.vehicleListResponse)
        val options = RequestOptions(
            headers = mapOf("X-Custom-Header" to "test-value"),
            urlParameters = mapOf("param" to "value")
        )

        val response = client.getVehicleList(options)

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    // ==================== Request Verification ====================

    @Test
    fun getVehicleDetails_makesCorrectRequest() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, ConnectedVehicleFixtures.vehicleDetailsResponse)
        )
        val client = createTestClient(capturingEngine.engine)

        client.getVehicleDetails(TestData.TEST_VIN)

        assertEquals(1, capturingEngine.requests.size)
        val request = capturingEngine.requests.first()
        assertTrue(request.url.encodedPath.contains("connected-vehicle/v2/vehicles/${TestData.TEST_VIN}"))
    }

    @Test
    fun invokeLock_makesPostRequest() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, ConnectedVehicleFixtures.commandResponse)
        )
        val client = createTestClient(capturingEngine.engine)

        client.invokeLock(TestData.TEST_VIN)

        assertEquals(1, capturingEngine.requests.size)
        val request = capturingEngine.requests.first()
        assertEquals(HttpMethod.Post, request.method)
        assertTrue(request.url.encodedPath.contains("commands/lock"))
    }
}
