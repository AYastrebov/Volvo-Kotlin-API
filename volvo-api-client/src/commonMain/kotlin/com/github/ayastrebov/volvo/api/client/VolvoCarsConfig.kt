package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.core.ProxyConfig
import com.github.ayastrebov.volvo.api.core.RetryStrategy
import com.github.ayastrebov.volvo.api.http.Timeout
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlin.time.Duration.Companion.seconds

/**
 * Volvo API client configuration.
 *
 * @param apiKey VCC Api Key
 * @param token VCC Access Token
 * @param logging client logging configuration
 * @param timeout http client timeout
 * @param headers extra http headers
 * @param proxy HTTP proxy url
 * @param retry rate limit retry configuration
 * @param engine explicit ktor engine for http requests.
 * @param httpClientConfig additional custom client configuration
 */
public data class VolvoCarsConfig(
    public val apiKey: String,
    public val token: String,
    public val logging: LoggingConfig = LoggingConfig(),
    public val timeout: Timeout = Timeout(socket = 30.seconds),
    public val headers: Map<String, String> = emptyMap(),
    public val proxy: ProxyConfig? = null,
    public val retry: RetryStrategy = RetryStrategy(),
    public val engine: HttpClientEngine? = null,
    public val httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
)
