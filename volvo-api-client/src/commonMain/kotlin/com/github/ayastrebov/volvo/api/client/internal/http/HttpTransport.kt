package com.github.ayastrebov.volvo.api.client.internal.http

import com.github.ayastrebov.volvo.api.exception.AuthenticationException
import com.github.ayastrebov.volvo.api.exception.GenericIOException
import com.github.ayastrebov.volvo.api.exception.InvalidRequestException
import com.github.ayastrebov.volvo.api.exception.VolvoAPIException
import com.github.ayastrebov.volvo.api.exception.PermissionException
import com.github.ayastrebov.volvo.api.exception.RateLimitException
import com.github.ayastrebov.volvo.api.exception.UnknownException
import com.github.ayastrebov.volvo.api.exception.VolvoApiError
import com.github.ayastrebov.volvo.api.exception.VolvoException
import com.github.ayastrebov.volvo.api.exception.VolvoHttpException
import com.github.ayastrebov.volvo.api.exception.VolvoServerException
import com.github.ayastrebov.volvo.api.exception.VolvoTimeoutException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CancellationException

/** HTTP transport layer */
internal class HttpTransport(private val httpClient: HttpClient) : HttpRequester {

    /** Perform an HTTP request and get a result */
    override suspend fun <T : Any> perform(info: TypeInfo, block: suspend (HttpClient) -> HttpResponse): T {
        try {
            val response = block(httpClient)
            return response.body(info)
        } catch (e: Exception) {
            throw handleException(e)
        }
    }

    override suspend fun <T : Any> perform(
        builder: HttpRequestBuilder,
        block: suspend (response: HttpResponse) -> T
    ) {
        try {
            HttpStatement(builder = builder, client = httpClient).execute(block)
        } catch (e: Exception) {
            throw handleException(e)
        }
    }

    override fun close() {
        httpClient.close()
    }

    /**
     * Handles various exceptions that can occur during an API request and converts them into appropriate
     * [VolvoException] instances.
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
     * This function helps in handling specific API errors and categorizing them into appropriate exception classes.
     */
    private suspend fun volvoAPIException(exception: ClientRequestException): VolvoAPIException {
        val response = exception.response
        val status = response.status.value
        val error = response.body<VolvoApiError>()
        return when(status) {
            429 -> RateLimitException(status, error, exception)
            400, 404, 409, 415 -> InvalidRequestException(status, error, exception)
            401 -> AuthenticationException(status, error, exception)
            403 -> PermissionException(status, error, exception)
            else -> UnknownException(status, error, exception)
        }
    }
}
