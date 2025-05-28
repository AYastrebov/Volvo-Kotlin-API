package com.github.ayastrebov.volvo.api.client.internal

import com.github.ayastrebov.volvo.api.client.ProxyConfig
import com.github.ayastrebov.volvo.api.client.VolvoApiConfig
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
 * Default Http Client.
 */
internal fun createHttpClient(config: VolvoApiConfig): HttpClient {
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
            // retry on rate limit error.
            retryIf { _, response -> response.status.value.let { it == 429 } }
            exponentialDelay(config.retry.base, config.retry.maxDelay.inWholeMilliseconds)
        }

        install(SSE)

        defaultRequest {
            url(ApiConfig.API_URL)
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
 * Internal Json Serializer.
 */
internal val JsonLenient = Json {
    isLenient = true
    ignoreUnknownKeys = true
}