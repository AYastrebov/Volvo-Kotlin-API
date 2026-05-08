package com.github.ayastrebov.volvo.api.client.internal

import com.github.ayastrebov.volvo.api.client.VolvoCarsConfig
import com.github.ayastrebov.volvo.api.core.ProxyConfig
import com.github.ayastrebov.volvo.api.client.internal.extension.toKtorLogLevel
import com.github.ayastrebov.volvo.api.client.internal.extension.toKtorLogger
import com.github.ayastrebov.volvo.client.ApiConfig
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json
import kotlin.time.DurationUnit

/**
 * Creates and configures the HTTP client for Volvo API communication.
 *
 * Configuration includes:
 * - **Proxy**: Supports HTTP and SOCKS proxies via [ProxyConfig]
 * - **JSON Serialization**: Lenient parsing with unknown keys ignored
 * - **Logging**: Configurable log level with Authorization header sanitization
 * - **Authentication**: Bearer token authentication (refresh must be handled externally)
 * - **Timeouts**: Configurable socket, connect, and request timeouts
 * - **Retry**: Automatic retry with exponential backoff on rate limit (429) responses
 * - **SSE**: Server-Sent Events support for streaming responses
 * - **Headers**: User-Agent and VCC API key automatically included
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
                    BearerTokens(accessToken = config.token, refreshToken = "")
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
            maxRetries = config.retry.maxRetries
            retryIf { _, response -> response.status.value in config.retry.retryOnStatusCodes }
            exponentialDelay(
                base = config.retry.base,
                maxDelayMs = config.retry.maxDelay.inWholeMilliseconds,
                randomizationMs = (config.retry.maxDelay.inWholeMilliseconds * 0.25).toLong(),
            )
        }

        install(SSE)

        defaultRequest {
            url(ApiConfig.API_URL)
            headers {
                append(HttpHeaders.UserAgent, "VolvoCars-Kotlin-Client/${ApiConfig.VERSION}")
                append("vcc-api-key", config.apiKey)
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
 * Lenient JSON serializer configured for Volvo API responses.
 *
 * - `isLenient = true`: Accepts non-standard JSON (e.g., unquoted strings, trailing commas)
 * - `ignoreUnknownKeys = true`: Allows API responses to include additional fields without causing deserialization errors
 */
internal val JsonLenient = Json {
    isLenient = true
    ignoreUnknownKeys = true
}