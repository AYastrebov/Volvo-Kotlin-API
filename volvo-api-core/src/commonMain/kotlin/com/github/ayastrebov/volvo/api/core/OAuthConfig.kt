package com.github.ayastrebov.volvo.api.core

/**
 * OAuth2 configuration for automatic token refresh.
 *
 * When provided, the client will automatically refresh the access token using
 * the Volvo ID token endpoint when a 401 response is received.
 *
 * Volvo ID uses refresh token rotation — each refresh returns a new refresh token
 * and invalidates the previous one. The [onTokenRefreshed] callback allows you to
 * persist the new tokens.
 *
 * Token lifecycle:
 * - Access tokens are short-lived (check `expires_in` from the token response)
 * - Refresh tokens must be used within **7 days** or they expire
 * - Maximum grant lifetime is **6 months** with continuous refreshing
 *
 * @property tokenUrl The Volvo ID token endpoint URL
 * @property clientId OAuth2 client ID from the Volvo Developer Portal
 * @property clientSecret OAuth2 client secret from the Volvo Developer Portal
 * @property refreshToken The current refresh token obtained during authorization
 * @property onTokenRefreshed Callback invoked when tokens are refreshed, providing
 *   the new access token and refresh token for persistence
 *
 * @see [Volvo Authorization Docs](https://developer.volvocars.com/apis/docs/authorisation/)
 */
public data class OAuthConfig(
    public val tokenUrl: String = TOKEN_URL,
    public val clientId: String,
    public val clientSecret: String,
    public val refreshToken: String,
    public val onTokenRefreshed: ((accessToken: String, refreshToken: String) -> Unit)? = null,
) {
    public companion object {
        /** Default Volvo ID token endpoint. */
        public const val TOKEN_URL: String = "https://volvoid.eu.volvocars.com/as/token.oauth2"
    }
}
