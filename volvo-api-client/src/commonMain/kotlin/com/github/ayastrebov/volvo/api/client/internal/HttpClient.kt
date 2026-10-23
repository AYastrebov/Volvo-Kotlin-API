package com.github.ayastrebov.volvo.api.client.internal

import com.github.ayastrebov.volvo.api.client.VolvoCarsConfig
import com.github.ayastrebov.volvo.api.client.internal.extension.toKtorLogLevel
import com.github.ayastrebov.volvo.api.client.internal.extension.toKtorLogger
import com.github.ayastrebov.volvo.api.core.OAuthConfig
import com.github.ayastrebov.volvo.api.core.ProxyConfig
import com.github.ayastrebov.volvo.api.core.Traceparent
import com.github.ayastrebov.volvo.api.exception.AuthenticationException
import com.github.ayastrebov.volvo.api.exception.InvalidRequestException
import com.github.ayastrebov.volvo.api.exception.VolvoApiError
import com.github.ayastrebov.volvo.api.exception.VolvoErrorDetails
import com.github.ayastrebov.volvo.api.exception.VolvoException
import com.github.ayastrebov.volvo.client.ApiConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.util.*
import io.ktor.util.date.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.pow
import kotlin.time.DurationUnit

/**
 * Creates and configures the HTTP client for Volvo API communication.
 *
 * Configuration includes:
 * - **Proxy**: Supports HTTP and SOCKS proxies via [ProxyConfig]
 * - **JSON Serialization**: Lenient parsing with unknown keys ignored
 * - **Logging**: Configurable log level with Authorization header sanitization
 * - **Authentication**: Bearer token with optional automatic refresh via [OAuthConfig]
 * - **Timeouts**: Configurable socket, connect, and request timeouts
 * - **Retry**: Automatic retry with exponential backoff, honouring `Retry-After` (seconds or HTTP date)
 * - **Headers**: User-Agent, VCC API key, a per-request `traceparent` and any [VolvoCarsConfig.headers]
 *
 * `expectSuccess = true` ensures non-2xx responses throw exceptions for proper error handling.
 *
 * @param config The [VolvoCarsConfig] containing all client settings
 * @return Configured [HttpClient] instance
 */
internal fun createHttpClient(config: VolvoCarsConfig): HttpClient {
    val configuration:  HttpClientConfig<*>.() -> Unit = {
        engine {
            config.proxy?.let { proxyConfig ->
                proxy = when (proxyConfig) {
                    is ProxyConfig.Http -> ProxyBuilder.http(Url(proxyConfig.url))
                    is ProxyConfig.Socks -> ProxyBuilder.socks(proxyConfig.host, proxyConfig.port)
                }
            }
        }

        install(ContentNegotiation) {
            register(ContentType.Application.Json, KotlinxSerializationConverter(JsonLenient))
        }

        install(Logging) {
            val logging = config.logging
            logger = logging.logger.toKtorLogger()
            level = logging.logLevel.toKtorLogLevel()
            if (logging.sanitize) {
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = config.accessToken,
                        refreshToken = config.oauth?.refreshToken ?: ""
                    )
                }
                config.oauth?.let { oauth ->
                    refreshTokens {
                        val response = refreshTokenRequest(oauth)
                        // Volvo ID may omit refresh_token when it does not rotate it; keep using the old one then.
                        val refreshToken = response.refreshToken ?: oldTokens?.refreshToken ?: oauth.refreshToken
                        oauth.onTokensRefreshed?.invoke(response.accessToken, refreshToken)
                        BearerTokens(
                            accessToken = response.accessToken,
                            refreshToken = refreshToken
                        )
                    }
                }
            }
        }

        install(HttpTimeout) {
            config.timeout.socket?.let { socketTimeout ->
                socketTimeoutMillis = socketTimeout.toLong(DurationUnit.MILLISECONDS)
            }
            config.timeout.connect?.let { connectTimeout ->
                connectTimeoutMillis = connectTimeout.toLong(DurationUnit.MILLISECONDS)
            }
            config.timeout.request?.let { requestTimeout ->
                requestTimeoutMillis = requestTimeout.toLong(DurationUnit.MILLISECONDS)
            }
        }

        install(HttpRequestRetry) {
            var retryAfterMs = 0L
            maxRetries = config.retry.maxRetries
            retryIf { _, response ->
                val shouldRetry = response.status.value in config.retry.retryOnStatusCodes
                if (shouldRetry) {
                    retryAfterMs = response.headers[HttpHeaders.RetryAfter].toRetryAfterMillis()
                }
                shouldRetry
            }
            delayMillis { retry ->
                val serverDelay = retryAfterMs.also { retryAfterMs = 0 }
                if (serverDelay > 0) {
                    serverDelay.coerceAtMost(config.retry.maxDelay.inWholeMilliseconds)
                } else {
                    val exponentialMs = (config.retry.base.pow(retry) * 1000)
                        .toLong()
                        .coerceAtMost(config.retry.maxDelay.inWholeMilliseconds)
                    val jitterMs = (exponentialMs * 0.25 * kotlin.random.Random.nextDouble()).toLong()
                    exponentialMs + jitterMs
                }
            }
        }

        // Runs for every request. The builder already holds the request's own headers, so
        // appendIfNameAbsent gives precedence: RequestOptions > VolvoCarsConfig.headers > built-in defaults.
        defaultRequest {
            url(ApiConfig.API_URL)
            headers {
                config.headers.forEach { (name, value) -> appendIfNameAbsent(name, value) }
                appendIfNameAbsent(HttpHeaders.UserAgent, "VolvoCars-Kotlin-Client/${ApiConfig.VERSION}")
                appendIfNameAbsent("vcc-api-key", config.apiKey)
                appendIfNameAbsent(Traceparent.HEADER, Traceparent.generate().value)
            }
        }

        expectSuccess = true

        config.httpClientConfig(this)
    }

    return if(config.engine != null) {
        HttpClient(config.engine, configuration)
    } else {
        HttpClient(configuration)
    }
}

/**
 * Parses a `Retry-After` header, which is either a delay in seconds or an HTTP date.
 *
 * @return the delay in milliseconds, or 0 when the header is absent, unparseable or in the past
 */
internal fun String?.toRetryAfterMillis(): Long {
    if (this == null) return 0
    toLongOrNull()?.let { return it * 1000 }
    return runCatching { fromHttpToGmtDate().timestamp - GMTDate().timestamp }
        .getOrDefault(0L)
        .coerceAtLeast(0L)
}

/**
 * Refreshes the OAuth2 access token using the Volvo ID token endpoint.
 *
 * Volvo ID answers a stale or revoked refresh token with 400, so 400/401/403 from the token
 * endpoint surface as [AuthenticationException] rather than a generic invalid request.
 */
private suspend fun RefreshTokensParams.refreshTokenRequest(oauth: OAuthConfig): TokenResponse {
    val response = try {
        client.submitForm(
            url = oauth.tokenUrl,
            formParameters = parameters {
                append("grant_type", "refresh_token")
                append("refresh_token", oldTokens?.refreshToken ?: oauth.refreshToken)
            }
        ) {
            // client_secret_basic. Note: this must be the request builder's header(), not the
            // top-level io.ktor.http.headers {} factory, which would build and discard a Headers object.
            basicAuth(username = oauth.clientId, password = oauth.clientSecret)
            markAsRefreshTokenRequest()
        }
    } catch (e: ClientRequestException) {
        throw e.toTokenRefreshException()
    }
    return response.body()
}

private suspend fun ClientRequestException.toTokenRefreshException(): VolvoException {
    val status = response.status.value
    val oauthError = runCatching { response.body<OAuthErrorResponse>() }.getOrNull()
    val error = VolvoApiError(
        status = status,
        detail = VolvoErrorDetails(
            code = oauthError?.error,
            message = "Token refresh failed: ${oauthError?.errorDescription ?: oauthError?.error ?: response.status.description}",
        )
    )
    return when (status) {
        400, 401, 403 -> AuthenticationException(status, error, this)
        else -> InvalidRequestException(status, error, this)
    }
}

/**
 * Token response from the Volvo ID OAuth2 token endpoint.
 */
@Serializable
internal data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String = "Bearer",
    @SerialName("expires_in") val expiresIn: Int = 0,
)

/**
 * Error body from the OAuth2 token endpoint (RFC 6749 §5.2).
 */
@Serializable
internal data class OAuthErrorResponse(
    @SerialName("error") val error: String? = null,
    @SerialName("error_description") val errorDescription: String? = null,
)

/**
 * Lenient JSON serializer configured for Volvo API responses.
 *
 * - `isLenient = true`: Accepts non-standard JSON (e.g., unquoted strings, trailing commas)
 * - `ignoreUnknownKeys = true`: Allows API responses to include additional fields without causing deserialization errors
 */
internal val JsonLenient = Json {
    isLenient = true
    ignoreUnknownKeys = true
}
