@file:OptIn(InternalVolvoApi::class)

package com.github.ayastrebov.volvo.api.client.test

import com.github.ayastrebov.volvo.api.InternalVolvoApi
import com.github.ayastrebov.volvo.api.client.VolvoCars
import com.github.ayastrebov.volvo.api.client.VolvoCarsConfig
import com.github.ayastrebov.volvo.api.client.internal.VolvoCarsApi
import com.github.ayastrebov.volvo.api.client.internal.http.HttpTransport
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Creates a test VolvoCars client with a mock HTTP engine.
 *
 * @param mockEngine The mock engine to use for HTTP requests
 * @return A VolvoCars instance configured with the mock engine
 */
fun createTestClient(mockEngine: MockEngine): VolvoCars {
    val httpClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        expectSuccess = true
    }
    val transport = HttpTransport(httpClient)
    return VolvoCarsApi(transport)
}

/**
 * Creates a test VolvoCars client with a single mock response.
 *
 * @param responseBody The JSON response body
 * @param status The HTTP status code (default: 200 OK)
 * @return A VolvoCars instance configured to return the specified response
 */
fun createTestClientWithResponse(
    responseBody: String,
    status: HttpStatusCode = HttpStatusCode.OK
): VolvoCars {
    val mockEngine = createSingleResponseMockEngine(responseBody, status)
    return createTestClient(mockEngine)
}

/**
 * Creates a test VolvoCars client with multiple mock responses mapped by path.
 *
 * @param responses Map of path patterns to MockResponse objects
 * @return A VolvoCars instance configured with the specified responses
 */
fun createTestClientWithResponses(
    responses: Map<String, MockResponse>
): VolvoCars {
    val mockEngine = createMockEngine(responses)
    return createTestClient(mockEngine)
}

/**
 * Creates a VolvoCarsConfig with a mock engine for testing.
 *
 * @param mockEngine The mock engine to use
 * @return A VolvoCarsConfig instance configured with test credentials and the mock engine
 */
fun createTestConfig(mockEngine: MockEngine): VolvoCarsConfig {
    return VolvoCarsConfig(
        apiKey = TestData.TEST_API_KEY,
        token = TestData.TEST_TOKEN,
        engine = mockEngine
    )
}
