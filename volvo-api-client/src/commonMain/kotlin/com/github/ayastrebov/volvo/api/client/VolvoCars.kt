package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.api.ConnectedVehicle
import com.github.ayastrebov.volvo.api.api.Energy
import com.github.ayastrebov.volvo.api.api.Location
import com.github.ayastrebov.volvo.api.client.internal.VolvoCarsApi
import com.github.ayastrebov.volvo.api.client.internal.createHttpClient
import com.github.ayastrebov.volvo.api.client.internal.http.HttpTransport
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.core.ProxyConfig
import com.github.ayastrebov.volvo.api.core.RetryStrategy
import com.github.ayastrebov.volvo.api.http.Timeout
import io.ktor.client.*
import kotlin.time.Duration.Companion.seconds

/**
 * Main entry point for Volvo Vehicle API operations.
 *
 * Combines [ConnectedVehicle], [Energy], and [Location] APIs into a single client.
 *
 * **Thread Safety:** This client is safe for concurrent coroutine execution.
 * A single instance can handle multiple simultaneous requests.
 *
 * **Resource Management:** Create one client and reuse it across your application.
 * Call [close] when done to release underlying HTTP resources.
 * Calling [close] multiple times is safe.
 *
 * **Authentication:** By default, the client uses a static OAuth2 Bearer token.
 * For automatic token refresh, provide [OAuthConfig][com.github.ayastrebov.volvo.api.core.OAuthConfig]
 * with your OAuth2 client credentials. The client will automatically refresh
 * expired tokens using the Volvo ID token endpoint.
 *
 * ```kotlin
 * val client = VolvoCars(apiKey = "your-key", token = "your-token")
 * try {
 *     val vehicles = client.getVehicleList()
 * } finally {
 *     client.close()
 * }
 * ```
 *
 * @see VolvoCarsConfig for detailed configuration options
 */
public interface VolvoCars : ConnectedVehicle, Location, Energy, AutoCloseable

/**
 * Creates an instance of [VolvoCars] with individual configuration parameters.
 *
 * This factory function provides a convenient way to create a Volvo API client
 * without manually constructing a [VolvoCarsConfig] object.
 *
 * @param apiKey The VCC API key obtained from the Volvo developer portal
 * @param token The OAuth2 access token for authenticating API requests
 * @param logging Configuration for HTTP request/response logging (default: no logging)
 * @param timeout Timeout configuration for HTTP requests (default: 30 seconds socket timeout)
 * @param headers Additional HTTP headers to include in all requests
 * @param proxy Optional proxy configuration for HTTP requests
 * @param retry Retry strategy configuration for failed requests (default: no retries)
 * @param httpClientConfig Additional Ktor HttpClient configuration block
 * @return A configured [VolvoCars] instance ready for API calls
 *
 * @see VolvoCars Overload accepting [VolvoCarsConfig] for more complex configurations
 * @see VolvoCarsConfig For detailed configuration options
 */
public fun VolvoCars(
    apiKey: String,
    token: String,
    logging: LoggingConfig = LoggingConfig(),
    timeout: Timeout = Timeout(socket = 30.seconds),
    headers: Map<String, String> = emptyMap(),
    proxy: ProxyConfig? = null,
    retry: RetryStrategy = RetryStrategy(),
    httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
): VolvoCars = VolvoCars(
    config = VolvoCarsConfig(
        apiKey = apiKey,
        token = token,
        logging = logging,
        timeout = timeout,
        headers = headers,
        proxy = proxy,
        retry = retry,
        httpClientConfig = httpClientConfig,
    )
)

/**
 * Creates an instance of [VolvoCarsApi].
 *
 * @param config client config
 */
public fun VolvoCars(config: VolvoCarsConfig): VolvoCars {
    val httpClient = createHttpClient(config)
    val transport = HttpTransport(httpClient)
    return VolvoCarsApi(transport)
}