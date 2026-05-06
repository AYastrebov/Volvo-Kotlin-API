package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.api.ConnectedVehicle
import com.github.ayastrebov.volvo.api.api.Energy
import com.github.ayastrebov.volvo.api.api.Location
import com.github.ayastrebov.volvo.api.client.test.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for VolvoCars client lifecycle and interface contracts.
 *
 * Tests verify:
 * - Client creation via constructor and factory function
 * - AutoCloseable implementation and use{} block behavior
 * - Implementation of all API interfaces
 */
class VolvoCarsClientTest {

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

        assertIs<AutoCloseable>(client)
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

        assertIs<VolvoCars>(client)
        assertIs<ConnectedVehicle>(client)
        assertIs<Energy>(client)
        assertIs<Location>(client)
        assertIs<AutoCloseable>(client)

        client.close()
    }
}
