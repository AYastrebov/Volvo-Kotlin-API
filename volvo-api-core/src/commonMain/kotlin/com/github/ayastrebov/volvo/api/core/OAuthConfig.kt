package com.github.ayastrebov.volvo.api.core

/**
 * OAuth2 authentication configuration for the Volvo ID identity system.
 *
 * Handles the complete OAuth2 Bearer token lifecycle including automatic refresh.
 * When the access token expires (401 response), the client automatically requests
 * a new one from the Volvo ID token endpoint using the refresh token.
 *
 * **Refresh token rotation:** Volvo ID returns a new refresh token with each refresh.
 * The old refresh token is immediately invalidated. Use [onTokensRefreshed] to persist
 * the new tokens so they survive app restarts.
 *
 * **Token lifecycle:**
 * - Access tokens are short-lived (typically minutes)
 * - Refresh tokens expire after **7 days** of inactivity
 * - Maximum grant lifetime is **6 months** with continuous refreshing
 * - After 6 months the user must re-authenticate
 *
 * ```kotlin
 * val auth = OAuthConfig(
 *     accessToken = storedAccessToken,
 *     refreshToken = storedRefreshToken,
 *     clientId = "your-client-id",
 *     clientSecret = "your-client-secret",
 *     onTokensRefreshed = { newAccess, newRefresh ->
 *         storage.save(newAccess, newRefresh)
 *     }
 * )
 * ```
 *
 * @property accessToken The current OAuth2 access token (Bearer token for API calls)
 * @property refreshToken The current refresh token for obtaining new access tokens
 * @property clientId OAuth2 client ID from the [Volvo Developer Portal](https://developer.volvocars.com/)
 * @property clientSecret OAuth2 client secret from the Volvo Developer Portal
 * @property tokenUrl The Volvo ID token endpoint URL (override for testing)
 * @property onTokensRefreshed Callback invoked after a successful token refresh with the
 *   new access token and new refresh token. **Must persist both tokens** — the old
 *   refresh token is invalidated immediately.
 */
public data class OAuthConfig(
    public val accessToken: String,
    public val refreshToken: String,
    public val clientId: String,
    public val clientSecret: String,
    public val tokenUrl: String = TOKEN_URL,
    public val onTokensRefreshed: ((accessToken: String, refreshToken: String) -> Unit)? = null,
) {
    public companion object {
        /** Default Volvo ID token endpoint. */
        public const val TOKEN_URL: String = "https://volvoid.eu.volvocars.com/as/token.oauth2"
    }
}
