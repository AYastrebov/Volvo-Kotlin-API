@file:OptIn(InternalVolvoApi::class)

package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.InternalVolvoApi
import com.github.ayastrebov.volvo.api.client.internal.http.HttpTransport
import com.github.ayastrebov.volvo.api.exception.*
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Unit tests for [HttpTransport] exception handling and request execution.
 */
class HttpTransportTest {

    private fun createTransport(mockEngine: MockEngine): HttpTransport {
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            expectSuccess = true
        }
        return HttpTransport(client)
    }

    // ==================== Status Code Mapping ====================

    @Test
    fun status400_mapsToInvalidRequestException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "BAD_REQUEST", "message": "Invalid request"}}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<InvalidRequestException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(400, exception.statusCode)
    }

    @Test
    fun status401_mapsToAuthenticationException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "UNAUTHORIZED", "message": "Invalid token"}}""",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<AuthenticationException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(401, exception.statusCode)
    }

    @Test
    fun status403_mapsToPermissionException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "FORBIDDEN", "message": "Access denied"}}""",
                status = HttpStatusCode.Forbidden,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<PermissionException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(403, exception.statusCode)
    }

    @Test
    fun status404_mapsToInvalidRequestException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "NOT_FOUND", "message": "Resource not found"}}""",
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<InvalidRequestException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(404, exception.statusCode)
    }

    @Test
    fun status409_mapsToInvalidRequestException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "CONFLICT", "message": "Conflict error"}}""",
                status = HttpStatusCode.Conflict,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<InvalidRequestException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(409, exception.statusCode)
    }

    @Test
    fun status415_mapsToInvalidRequestException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "UNSUPPORTED_MEDIA_TYPE", "message": "Invalid content type"}}""",
                status = HttpStatusCode.UnsupportedMediaType,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<InvalidRequestException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(415, exception.statusCode)
    }

    @Test
    fun status429_mapsToRateLimitException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "RATE_LIMITED", "message": "Too many requests"}}""",
                status = HttpStatusCode.TooManyRequests,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<RateLimitException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(429, exception.statusCode)
    }

    @Test
    fun status500_mapsToVolvoServerException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "INTERNAL_ERROR", "message": "Server error"}}""",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        assertFailsWith<VolvoServerException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }
    }

    @Test
    fun status502_mapsToVolvoServerException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "BAD_GATEWAY", "message": "Bad gateway"}}""",
                status = HttpStatusCode.BadGateway,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        assertFailsWith<VolvoServerException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }
    }

    @Test
    fun status503_mapsToVolvoServerException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "SERVICE_UNAVAILABLE", "message": "Service down"}}""",
                status = HttpStatusCode.ServiceUnavailable,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        assertFailsWith<VolvoServerException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }
    }

    @Test
    fun otherClientError_mapsToInvalidRequestException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "UNKNOWN", "message": "Unknown error"}}""",
                status = HttpStatusCode(418, "I'm a teapot"),
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        // All other 4xx client errors should map to InvalidRequestException
        val exception = assertFailsWith<InvalidRequestException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(418, exception.statusCode)
    }

    // ==================== Error Body Parsing ====================

    @Test
    fun invalidJsonBody_fallsBackToStatusDescription() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = "not json",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<InvalidRequestException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        // Should not crash, should create VolvoApiError with status description
        assertEquals(400, exception.statusCode)
    }

    @Test
    fun emptyBody_fallsBackToStatusDescription() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<AuthenticationException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals(401, exception.statusCode)
    }

    @Test
    fun errorResponse_preservesErrorDetails() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "INVALID_VIN", "message": "VIN format is invalid", "param": "vin"}}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<InvalidRequestException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertEquals("INVALID_VIN", exception.error.detail?.code)
        assertEquals("VIN format is invalid", exception.error.detail?.message)
        assertEquals("vin", exception.error.detail?.param)
    }

    // ==================== Exception Hierarchy ====================

    @Test
    fun allApiExceptions_extendVolvoAPIException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "ERROR", "message": "Error"}}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<VolvoAPIException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertIs<InvalidRequestException>(exception)
    }

    @Test
    fun allVolvoAPIExceptions_extendVolvoException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = """{"error": {"code": "ERROR", "message": "Error"}}""",
                status = HttpStatusCode.Forbidden,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<VolvoException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertIs<VolvoAPIException>(exception)
        assertIs<PermissionException>(exception)
    }

    // ==================== Transport Close ====================

    @Test
    fun close_doesNotThrowException() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = "ok",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json()
            }
        }
        val transport = HttpTransport(client)

        // Should not throw an exception
        transport.close()
    }
}
