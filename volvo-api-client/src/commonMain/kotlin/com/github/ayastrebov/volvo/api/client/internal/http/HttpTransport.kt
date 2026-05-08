package com.github.ayastrebov.volvo.api.client.internal.http

import com.github.ayastrebov.volvo.api.core.CircuitBreaker
import com.github.ayastrebov.volvo.api.exception.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

/** HTTP transport layer with optional circuit breaker. */
internal class HttpTransport(
    private val httpClient: HttpClient,
    private val circuitBreaker: CircuitBreaker? = null,
) : HttpRequester {

    /** Perform an HTTP request and get a result */
    override suspend fun <T : Any> perform(info: TypeInfo, block: suspend (HttpClient) -> HttpResponse): T {
        checkCircuitBreaker()
        try {
            val response = block(httpClient)
            circuitBreaker?.recordSuccess()
            return response.body(info)
        } catch (e: Exception) {
            val mapped = handleException(e)
            circuitBreaker?.recordFailure()
            throw mapped
        }
    }

    override suspend fun <T : Any> perform(
        builder: HttpRequestBuilder,
        block: suspend (response: HttpResponse) -> T
    ) {
        checkCircuitBreaker()
        try {
            HttpStatement(builder = builder, client = httpClient).execute(block)
            circuitBreaker?.recordSuccess()
        } catch (e: Exception) {
            val mapped = handleException(e)
            circuitBreaker?.recordFailure()
            throw mapped
        }
    }

    override fun close() {
        httpClient.close()
    }

    private fun checkCircuitBreaker() {
        if (circuitBreaker != null && !circuitBreaker.allowRequest()) {
            throw RateLimitException(
                statusCode = 429,
                error = VolvoApiError(
                    detail = VolvoErrorDetails(
                        message = "Circuit breaker is open — too many consecutive failures. " +
                                "Requests will resume after the reset timeout."
                    )
                )
            )
        }
    }

    /**
     * Handles various exceptions that can occur during an API request and converts them into appropriate
     * [VolvoException] instances.
     *
     * Exception mapping:
     * - [CancellationException] → propagated as-is (coroutine cancellation)
     * - [ClientRequestException] → mapped to [VolvoAPIException] subclass based on status code
     * - [ServerResponseException] → [VolvoServerException] for 5xx errors
     * - [HttpRequestTimeoutException], [SocketTimeoutException], [ConnectTimeoutException] → [VolvoTimeoutException]
     * - [IOException] → [GenericIOException] for network failures
     * - Other → [VolvoHttpException] as fallback
     */
    private suspend fun handleException(e: Throwable) = when (e) {
        is CancellationException -> e // propagate coroutine cancellation
        is ClientRequestException -> volvoAPIException(e)
        is ServerResponseException -> VolvoServerException(e)
        is HttpRequestTimeoutException, is SocketTimeoutException, is ConnectTimeoutException -> VolvoTimeoutException(
            e
        )
        is IOException -> GenericIOException(e)
        else -> VolvoHttpException(e)
    }

    /**
     * Converts a [ClientRequestException] into a corresponding [VolvoAPIException] based on the HTTP status code.
     *
     * Status code mapping:
     * - 401 → [AuthenticationException]
     * - 403 → [PermissionException]
     * - 429 → [RateLimitException]
     * - Other 4xx → [InvalidRequestException]
     */
    private suspend fun volvoAPIException(exception: ClientRequestException): VolvoAPIException {
        val response = exception.response
        val status = response.status.value
        val error = try {
            response.body<VolvoApiError>()
        } catch (e: Exception) {
            VolvoApiError(detail = VolvoErrorDetails(message = response.status.description))
        }
        return when (status) {
            401 -> AuthenticationException(status, error, exception)
            403 -> PermissionException(status, error, exception)
            429 -> RateLimitException(status, error, exception)
            else -> InvalidRequestException(status, error, exception)
        }
    }
}
