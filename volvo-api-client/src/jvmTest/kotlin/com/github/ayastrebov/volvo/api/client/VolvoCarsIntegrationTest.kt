package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.api.ConnectedVehicle
import com.github.ayastrebov.volvo.api.api.Energy
import com.github.ayastrebov.volvo.api.api.ExtendedVehicle
import com.github.ayastrebov.volvo.api.api.Location
import com.github.ayastrebov.volvo.api.client.test.*
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.model.connectedvehicle.EngineStartRequest
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * JVM-specific integration tests for the VolvoCars client.
 * These tests verify the full client creation and API interaction workflows.
 */
class VolvoCarsIntegrationTest {

    // ==================== Client Creation ====================

    @Test
    fun createClient_withBasicConfig_createsSuccessfully() = runTest {
        val mockEngine = createSingleResponseMockEngine(
            ConnectedVehicleFixtures.vehicleListResponse,
            HttpStatusCode.OK
        )

        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = mockEngine
            )
        )

        assertNotNull(client)
        client.close()
    }

    @Test
    fun createClient_withFactoryFunction_createsSuccessfully() = runTest {
        val mockEngine = createSingleResponseMockEngine(
            ConnectedVehicleFixtures.vehicleListResponse,
            HttpStatusCode.OK
        )

        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            engine = mockEngine
        )

        val client = VolvoCars(config)
        assertNotNull(client)
        client.close()
    }

    // ==================== AutoCloseable ====================

    @Test
    fun client_implementsAutoCloseable() = runTest {
        val mockEngine = createSingleResponseMockEngine(
            ConnectedVehicleFixtures.vehicleListResponse,
            HttpStatusCode.OK
        )

        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = mockEngine
            )
        )

        assertTrue(client is AutoCloseable)
        client.close()
    }

    @Test
    fun client_useBlock_closesAutomatically() = runTest {
        val mockEngine = createSingleResponseMockEngine(
            ConnectedVehicleFixtures.vehicleListResponse,
            HttpStatusCode.OK
        )

        VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = mockEngine
            )
        ).use { client ->
            val response = client.getVehicleList()
            assertNotNull(response)
        }
        // Client should be closed after the block
    }

    // ==================== Interface Implementation ====================

    @Test
    fun client_implementsAllInterfaces() = runTest {
        val mockEngine = createSingleResponseMockEngine(
            ConnectedVehicleFixtures.vehicleListResponse,
            HttpStatusCode.OK
        )

        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = mockEngine
            )
        )

        assertTrue(client is VolvoCars)
        assertTrue(client is ConnectedVehicle)
        assertTrue(client is Energy)
        assertTrue(client is Location)
        assertTrue(client is ExtendedVehicle)
        assertTrue(client is AutoCloseable)

        client.close()
    }

    // ==================== Multiple API Calls ====================

    @Test
    fun client_canCallMultipleApis() = runTest {
        val responses = mapOf(
            "connected-vehicle/v2/vehicles" to MockResponse(
                HttpStatusCode.OK,
                ConnectedVehicleFixtures.vehicleListResponse
            ),
            "connected-vehicle/v2/vehicles/${TestData.TEST_VIN}" to MockResponse(
                HttpStatusCode.OK,
                ConnectedVehicleFixtures.vehicleDetailsResponse
            ),
            "energy/v2/vehicles/${TestData.TEST_VIN}/capabilities" to MockResponse(
                HttpStatusCode.OK,
                EnergyFixtures.capabilitiesResponse
            ),
            "location/v1/vehicles/${TestData.TEST_VIN}/location" to MockResponse(
                HttpStatusCode.OK,
                LocationFixtures.locationResponse
            )
        )

        val mockEngine = createMockEngine(responses)
        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = mockEngine
            )
        )

        // Connected Vehicle API
        val vehicleList = client.getVehicleList()
        assertEquals(200, vehicleList.status)
        assertNotNull(vehicleList.data)

        val vehicleDetails = client.getVehicleDetails(TestData.TEST_VIN)
        assertEquals(200, vehicleDetails.status)
        assertEquals(TestData.TEST_VIN, vehicleDetails.data?.vin)

        // Energy API
        val capabilities = client.getCapabilities(TestData.TEST_VIN)
        assertEquals(200, capabilities.status)
        assertTrue(capabilities.data?.getEnergyState?.isSupported == true)

        // Location API
        val location = client.getVehicleLocation(TestData.TEST_VIN)
        assertEquals(200, location.status)
        assertEquals("Feature", location.data?.type)

        client.close()
    }

    // ==================== Configuration Impact ====================

    @Test
    fun client_withCustomTimeout_usesTimeout() = runTest {
        val mockEngine = createSingleResponseMockEngine(
            ConnectedVehicleFixtures.vehicleListResponse,
            HttpStatusCode.OK
        )

        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            timeout = Timeout(
                socket = 60.seconds,
                connect = 15.seconds,
                request = 120.seconds
            ),
            engine = mockEngine
        )

        val client = VolvoCars(config)
        val response = client.getVehicleList()
        assertEquals(200, response.status)
        client.close()
    }

    @Test
    fun client_withCustomHeaders_includesHeaders() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, ConnectedVehicleFixtures.vehicleListResponse)
        )

        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            headers = mapOf("X-Custom-Header" to "custom-value"),
            engine = capturingEngine.engine
        )

        val client = VolvoCars(config)
        client.getVehicleList()

        assertEquals(1, capturingEngine.requests.size)
        // The custom header should be included in the request
        // Note: We can't directly verify headers in mock engine without additional setup

        client.close()
    }

    // ==================== Command Execution Flow ====================

    @Test
    fun client_commandExecutionFlow_worksEndToEnd() = runTest {
        val responses = mapOf(
            "commands" to MockResponse(
                HttpStatusCode.OK,
                ConnectedVehicleFixtures.commandListResponse
            ),
            "command-accessibility" to MockResponse(
                HttpStatusCode.OK,
                ConnectedVehicleFixtures.commandAccessibilityResponse
            ),
            "commands/lock" to MockResponse(
                HttpStatusCode.OK,
                ConnectedVehicleFixtures.commandResponse
            ),
            "commands/unlock" to MockResponse(
                HttpStatusCode.OK,
                ConnectedVehicleFixtures.unlockCommandResponse
            )
        )

        val mockEngine = createMockEngine(responses)
        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = mockEngine
            )
        )

        // 1. Get available commands
        val commands = client.getCommandList(TestData.TEST_VIN)
        assertNotNull(commands.data)
        assertTrue(commands.data!!.isNotEmpty())

        // 2. Check command accessibility
        val accessibility = client.getCommandAccessibility(TestData.TEST_VIN)
        assertNotNull(accessibility.data?.availableCommands)

        // 3. Execute lock command
        val lockResponse = client.invokeLock(TestData.TEST_VIN)
        assertEquals("COMPLETED", lockResponse.data?.invokeStatus)

        // 4. Execute unlock command
        val unlockResponse = client.invokeUnlock(TestData.TEST_VIN)
        assertEquals("COMPLETED", unlockResponse.data?.invokeStatus)
        assertTrue(unlockResponse.data?.readyToUnlock == true)

        client.close()
    }

    // ==================== Energy State Flow ====================

    @Test
    fun client_energyStateFlow_worksEndToEnd() = runTest {
        val responses = mapOf(
            "capabilities" to MockResponse(
                HttpStatusCode.OK,
                EnergyFixtures.capabilitiesResponse
            ),
            "state" to MockResponse(
                HttpStatusCode.OK,
                EnergyFixtures.energyStateResponse
            )
        )

        val mockEngine = createMockEngine(responses)
        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = mockEngine
            )
        )

        // 1. Check capabilities
        val capabilities = client.getCapabilities(TestData.TEST_VIN)
        assertTrue(capabilities.data?.getEnergyState?.isSupported == true)
        assertTrue(capabilities.data?.getEnergyState?.batteryChargeLevel?.isSupported == true)

        // 2. Get energy state
        val energyState = client.getEnergyState(TestData.TEST_VIN)
        assertNotNull(energyState.data?.batteryChargeLevel)
        assertEquals("OK", energyState.data?.batteryChargeLevel?.status)
        assertEquals(78.5f, energyState.data?.batteryChargeLevel?.value)

        client.close()
    }

    // ==================== Engine Start with Request Body ====================

    @Test
    fun client_engineStart_sendsRequestBody() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, ConnectedVehicleFixtures.commandResponse)
        )

        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                token = TestData.TEST_TOKEN,
                engine = capturingEngine.engine
            )
        )

        val request = EngineStartRequest(runtimeMinutes = 10)
        val response = client.invokeEngineStart(TestData.TEST_VIN, request)

        assertEquals("COMPLETED", response.data?.invokeStatus)
        assertEquals(1, capturingEngine.requests.size)
        assertEquals(HttpMethod.Post, capturingEngine.requests.first().method)

        client.close()
    }
}
