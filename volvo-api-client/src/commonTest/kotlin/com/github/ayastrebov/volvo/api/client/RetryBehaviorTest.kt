package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.internal.createHttpClient
import com.github.ayastrebov.volvo.api.core.RetryStrategy
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for retry behavior with configurable retry strategy.
 */
class RetryBehaviorTest {

    // ==================== Retry Conditions ====================

    @Test
    fun retry_triggersOnConfiguredStatusCodes() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            if (requestCount <= 2) {
                respond(
                    content = "Rate limited",
                    status = HttpStatusCode.TooManyRequests,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            } else {
                respond(
                    content = "Success",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            }
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = setOf(429)
            )
        )

        val client = createHttpClient(config)
        val response: HttpResponse = client.get("https://api.volvocars.com/test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(3, requestCount) // 1 initial + 2 retries
    }

    @Test
    fun retry_triggersOn502WhenConfigured() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            if (requestCount <= 1) {
                respond(
                    content = "Bad Gateway",
                    status = HttpStatusCode.BadGateway,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            } else {
                respond(
                    content = "Success",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            }
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = setOf(429, 502, 503, 504)
            )
        )

        val client = createHttpClient(config)
        val response: HttpResponse = client.get("https://api.volvocars.com/test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(2, requestCount) // 1 initial + 1 retry
    }

    @Test
    fun retry_triggersOn503WhenConfigured() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            if (requestCount <= 1) {
                respond(
                    content = "Service Unavailable",
                    status = HttpStatusCode.ServiceUnavailable,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            } else {
                respond(
                    content = "Success",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            }
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = setOf(429, 502, 503, 504)
            )
        )

        val client = createHttpClient(config)
        val response: HttpResponse = client.get("https://api.volvocars.com/test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(2, requestCount) // 1 initial + 1 retry
    }

    @Test
    fun retry_triggersOn504WhenConfigured() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            if (requestCount <= 1) {
                respond(
                    content = "Gateway Timeout",
                    status = HttpStatusCode.GatewayTimeout,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            } else {
                respond(
                    content = "Success",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            }
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = setOf(429, 502, 503, 504)
            )
        )

        val client = createHttpClient(config)
        val response: HttpResponse = client.get("https://api.volvocars.com/test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(2, requestCount) // 1 initial + 1 retry
    }

    // ==================== Max Retries ====================

    @Test
    fun retry_respectsMaxRetriesLimit() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = "Rate limited",
                status = HttpStatusCode.TooManyRequests,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 2,
                retryOnStatusCodes = setOf(429)
            )
        )

        val client = createHttpClient(config)

        // With expectSuccess = true, non-2xx responses throw ClientRequestException
        assertFailsWith<ClientRequestException> {
            client.get("https://api.volvocars.com/test")
        }

        // Should be initial request + maxRetries
        assertEquals(3, requestCount) // 1 initial + 2 retries
    }

    @Test
    fun retry_withZeroMaxRetries_noRetries() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = "Rate limited",
                status = HttpStatusCode.TooManyRequests,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(maxRetries = 0)
        )

        val client = createHttpClient(config)

        // With expectSuccess = true, non-2xx responses throw ClientRequestException
        assertFailsWith<ClientRequestException> {
            client.get("https://api.volvocars.com/test")
        }

        // Should be only initial request, no retries
        assertEquals(1, requestCount)
    }

    // ==================== Non-Retry Status Codes ====================

    @Test
    fun retry_doesNotTriggerOn400() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = "Bad Request",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = setOf(429, 502, 503, 504)
            )
        )

        val client = createHttpClient(config)

        // 400 throws ClientRequestException and should not trigger retry
        assertFailsWith<ClientRequestException> {
            client.get("https://api.volvocars.com/test")
        }

        // Should not retry on 400
        assertEquals(1, requestCount)
    }

    @Test
    fun retry_doesNotTriggerOn401() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = "Unauthorized",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = setOf(429, 502, 503, 504)
            )
        )

        val client = createHttpClient(config)

        // 401 throws ClientRequestException and should not trigger retry
        assertFailsWith<ClientRequestException> {
            client.get("https://api.volvocars.com/test")
        }

        // Should not retry on 401
        assertEquals(1, requestCount)
    }

    @Test
    fun retry_doesNotTriggerOn500ByDefault() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                // Default includes 502, 503, 504 but not 500
                retryOnStatusCodes = setOf(429, 502, 503, 504)
            )
        )

        val client = createHttpClient(config)

        // 500 throws ServerResponseException and should not trigger retry
        assertFailsWith<ServerResponseException> {
            client.get("https://api.volvocars.com/test")
        }

        // Should not retry on 500 by default
        assertEquals(1, requestCount)
    }

    // ==================== Custom Retry Codes ====================

    @Test
    fun retry_canBeConfiguredFor500() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            if (requestCount <= 1) {
                respond(
                    content = "Internal Server Error",
                    status = HttpStatusCode.InternalServerError,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            } else {
                respond(
                    content = "Success",
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
                )
            }
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = setOf(429, 500, 502, 503, 504)
            )
        )

        val client = createHttpClient(config)
        val response: HttpResponse = client.get("https://api.volvocars.com/test")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(2, requestCount) // 1 initial + 1 retry
    }

    @Test
    fun retry_withEmptyStatusCodes_noRetries() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = "Rate limited",
                status = HttpStatusCode.TooManyRequests,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(
                maxRetries = 3,
                retryOnStatusCodes = emptySet()
            )
        )

        val client = createHttpClient(config)

        // With empty status codes, retry is disabled, but 429 still throws
        assertFailsWith<ClientRequestException> {
            client.get("https://api.volvocars.com/test")
        }

        // Should not retry with empty status codes set
        assertEquals(1, requestCount)
    }

    // ==================== Successful Requests ====================

    @Test
    fun retry_successfulRequestNoRetries() = runTest {
        var requestCount = 0
        val mockEngine = MockEngine { request ->
            requestCount++
            respond(
                content = "Success",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            engine = mockEngine,
            retry = RetryStrategy(maxRetries = 3)
        )

        val client = createHttpClient(config)
        val response: HttpResponse = client.get("https://api.volvocars.com/test")

        assertEquals(1, requestCount) // Only initial request
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
