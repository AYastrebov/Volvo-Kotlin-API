package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.core.OAuthConfig
import com.github.ayastrebov.volvo.api.core.ProxyConfig
import com.github.ayastrebov.volvo.api.core.RetryStrategy
import com.github.ayastrebov.volvo.api.http.Timeout
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlin.time.Duration.Companion.seconds

/**
 * Volvo API client configuration.
 *
 * @property apiKey VCC API key from the [Volvo Developer Portal](https://developer.volvocars.com/)
 * @property token OAuth2 Bearer access token from the Volvo ID identity system
 * @property oauth Optional OAuth2 configuration for automatic token refresh.
 *   When provided, the client will automatically refresh expired access tokens
 *   using the Volvo ID token endpoint. See [OAuthConfig] for details.
 * @property logging HTTP request/response logging configuration
 * @property timeout HTTP timeout configuration for request, connect, and socket timeouts
 * @property headers Additional HTTP headers to include in every request
 * @property proxy Proxy configuration ([ProxyConfig.Http] or [ProxyConfig.Socks])
 * @property retry Retry strategy with exponential backoff for transient errors (429, 5xx)
 * @property engine Explicit Ktor [HttpClientEngine] (useful for testing with mock engines)
 * @property httpClientConfig Additional Ktor HttpClient configuration block for advanced customization
 */
public data class VolvoCarsConfig(
    public val apiKey: String,
    public val token: String,
    public val oauth: OAuthConfig? = null,
    public val logging: LoggingConfig = LoggingConfig(),
    public val timeout: Timeout = Timeout(socket = 30.seconds),
    public val headers: Map<String, String> = emptyMap(),
    public val proxy: ProxyConfig? = null,
    public val retry: RetryStrategy = RetryStrategy(),
    public val engine: HttpClientEngine? = null,
    public val httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
)
