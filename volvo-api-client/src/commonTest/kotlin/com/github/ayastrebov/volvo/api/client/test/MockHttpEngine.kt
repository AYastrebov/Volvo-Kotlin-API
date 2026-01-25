package com.github.ayastrebov.volvo.api.client.test

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Mock response data for testing.
 */
data class MockResponse(
    val status: HttpStatusCode,
    val body: String,
    val headers: Headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
)

/**
 * Creates a mock HTTP engine that responds based on path matching.
 *
 * @param responses Map of URL path patterns to mock responses
 * @param defaultResponse Optional default response for unmatched paths
 */
fun createMockEngine(
    responses: Map<String, MockResponse>,
    defaultResponse: MockResponse? = null
): MockEngine {
    return MockEngine { request ->
        val path = request.url.encodedPath
        // Find all matching patterns and select the longest (most specific) match
        val response = responses.entries
            .filter { (pattern, _) ->
                path.endsWith(pattern) || path == pattern || path.contains(pattern)
            }
            .maxByOrNull { (pattern, _) -> pattern.length }
            ?.value ?: defaultResponse

        if (response != null) {
            respond(
                content = response.body,
                status = response.status,
                headers = response.headers
            )
        } else {
            error("Unmocked request: $path (available: ${responses.keys})")
        }
    }
}

/**
 * Creates a mock engine that always returns the same response.
 */
fun createSingleResponseMockEngine(
    body: String,
    status: HttpStatusCode = HttpStatusCode.OK
): MockEngine {
    return MockEngine {
        respond(
            content = body,
            status = status,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    }
}

/**
 * Creates a mock engine that captures requests for verification.
 */
class RequestCapturingMockEngine(
    private val response: MockResponse
) {
    private val _requests = mutableListOf<HttpRequestData>()
    val requests: List<HttpRequestData> get() = _requests

    val engine = MockEngine { request ->
        _requests.add(request)
        respond(
            content = response.body,
            status = response.status,
            headers = response.headers
        )
    }

    fun clear() {
        _requests.clear()
    }
}
