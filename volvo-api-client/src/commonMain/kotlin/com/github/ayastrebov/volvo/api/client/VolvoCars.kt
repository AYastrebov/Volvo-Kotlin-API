package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.api.ConnectedVehicle
import com.github.ayastrebov.volvo.api.api.Energy
import com.github.ayastrebov.volvo.api.api.Location
import com.github.ayastrebov.volvo.api.client.internal.VolvoCarsApi
import com.github.ayastrebov.volvo.api.client.internal.createHttpClient
import com.github.ayastrebov.volvo.api.client.internal.http.HttpTransport
import com.github.ayastrebov.volvo.api.core.OAuthConfig

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
 * **Authentication:** Supports two modes:
 * - **OAuth2 with automatic refresh** (recommended): Provide [OAuthConfig] with client
 *   credentials. The client automatically refreshes expired tokens via the Volvo ID
 *   token endpoint. Use [OAuthConfig.onTokensRefreshed] to persist rotated tokens.
 * - **Static token**: Provide a Bearer token directly. When it expires, create a
 *   new client with a fresh token.
 *
 * ```kotlin
 * // OAuth2 with automatic refresh (recommended)
 * val client = VolvoCars(
 *     VolvoCarsConfig(
 *         apiKey = "your-vcc-api-key",
 *         oauth = OAuthConfig(
 *             accessToken = storedAccessToken,
 *             refreshToken = storedRefreshToken,
 *             clientId = "your-client-id",
 *             clientSecret = "your-client-secret",
 *             onTokensRefreshed = { access, refresh -> save(access, refresh) }
 *         )
 *     )
 * )
 *
 * // Static token (testing / short-lived scripts)
 * val client = VolvoCars(apiKey = "your-key", token = "your-token")
 * ```
 *
 * @see VolvoCarsConfig for detailed configuration options
 * @see OAuthConfig for OAuth2 token refresh configuration
 */
public interface VolvoCars : ConnectedVehicle, Location, Energy, AutoCloseable

/**
 * Creates a [VolvoCars] client with a static Bearer token.
 *
 * This is a convenience factory for testing and short-lived scripts.
 * For production apps, use [VolvoCarsConfig] with [OAuthConfig] for automatic token refresh.
 *
 * @param apiKey The VCC API key from the Volvo Developer Portal
 * @param token The OAuth2 access token (will not be refreshed automatically)
 * @return A configured [VolvoCars] instance
 */
public fun VolvoCars(
    apiKey: String,
    token: String,
): VolvoCars = VolvoCars(
    config = VolvoCarsConfig(
        apiKey = apiKey,
        token = token,
    )
)

/**
 * Creates a [VolvoCars] client with full configuration.
 *
 * @param config Client configuration including authentication, timeouts, retry, and logging
 * @return A configured [VolvoCars] instance
 */
public fun VolvoCars(config: VolvoCarsConfig): VolvoCars {
    val httpClient = createHttpClient(config)
    val transport = HttpTransport(httpClient)
    return VolvoCarsApi(transport)
}
