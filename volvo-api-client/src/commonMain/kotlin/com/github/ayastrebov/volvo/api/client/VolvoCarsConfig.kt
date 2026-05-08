package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.core.CircuitBreakerConfig
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
 * Supports two authentication modes:
 *
 * **1. OAuth2 with automatic refresh (recommended):**
 * ```kotlin
 * VolvoCarsConfig(
 *     apiKey = "your-vcc-api-key",
 *     oauth = OAuthConfig(
 *         accessToken = storedAccessToken,
 *         refreshToken = storedRefreshToken,
 *         clientId = "your-client-id",
 *         clientSecret = "your-client-secret",
 *         onTokensRefreshed = { access, refresh -> save(access, refresh) }
 *     )
 * )
 * ```
 *
 * **2. Static token (for testing or short-lived scripts):**
 * ```kotlin
 * VolvoCarsConfig(
 *     apiKey = "your-vcc-api-key",
 *     token = "your-access-token"
 * )
 * ```
 *
 * @property apiKey VCC API key from the [Volvo Developer Portal](https://developer.volvocars.com/)
 * @property oauth OAuth2 configuration with automatic token refresh. Mutually exclusive with [token].
 * @property token Static Bearer access token. Use [oauth] instead for production apps.
 * @property logging HTTP request/response logging configuration
 * @property timeout HTTP timeout configuration for request, connect, and socket timeouts
 * @property headers Additional HTTP headers to include in every request
 * @property proxy Proxy configuration ([ProxyConfig.Http] or [ProxyConfig.Socks])
 * @property retry Retry strategy with exponential backoff for transient errors (429, 5xx).
 *   Honors `Retry-After` headers from 429 responses when present.
 * @property circuitBreaker Optional circuit breaker to stop retries after sustained failures.
 *   When open, requests fail immediately without hitting the server. See [CircuitBreakerConfig].
 * @property engine Explicit Ktor [HttpClientEngine] (useful for testing with mock engines)
 * @property httpClientConfig Additional Ktor HttpClient configuration block for advanced customization
 * @throws IllegalArgumentException if neither [oauth] nor [token] is provided, or if both are provided
 */
public data class VolvoCarsConfig(
    public val apiKey: String,
    public val oauth: OAuthConfig? = null,
    public val token: String? = null,
    public val logging: LoggingConfig = LoggingConfig(),
    public val timeout: Timeout = Timeout(socket = 30.seconds),
    public val headers: Map<String, String> = emptyMap(),
    public val proxy: ProxyConfig? = null,
    public val retry: RetryStrategy = RetryStrategy(),
    public val circuitBreaker: CircuitBreakerConfig? = null,
    public val engine: HttpClientEngine? = null,
    public val httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
) {
    init {
        require(oauth != null || token != null) {
            "Either 'oauth' or 'token' must be provided"
        }
        require(oauth == null || token == null) {
            "Provide either 'oauth' or 'token', not both"
        }
    }

    /** The access token to use for API requests. */
    internal val accessToken: String
        get() = oauth?.accessToken ?: token!!
}
