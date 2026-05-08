package com.github.ayastrebov.volvo.api.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Tests for OAuthConfig data class.
 */
class OAuthConfigTest {

    @Test
    fun defaultTokenUrl_isVolvoIdEndpoint() {
        val config = OAuthConfig(
            accessToken = "access",
            refreshToken = "refresh",
            clientId = "client-id",
            clientSecret = "client-secret",
        )
        assertEquals("https://volvoid.eu.volvocars.com/as/token.oauth2", config.tokenUrl)
        assertEquals(OAuthConfig.TOKEN_URL, config.tokenUrl)
    }

    @Test
    fun customTokenUrl_overridesDefault() {
        val customUrl = "https://custom.example.com/token"
        val config = OAuthConfig(
            accessToken = "access",
            refreshToken = "refresh",
            clientId = "client-id",
            clientSecret = "client-secret",
            tokenUrl = customUrl,
        )
        assertEquals(customUrl, config.tokenUrl)
    }

    @Test
    fun onTokensRefreshed_isOptional() {
        val config = OAuthConfig(
            accessToken = "access",
            refreshToken = "refresh",
            clientId = "client-id",
            clientSecret = "client-secret",
        )
        assertNull(config.onTokensRefreshed,
            "onTokensRefreshed must default to null when not provided")
    }

    @Test
    fun dataClass_equalityWorks() {
        val config1 = OAuthConfig(
            accessToken = "access",
            refreshToken = "refresh",
            clientId = "client-id",
            clientSecret = "client-secret",
        )
        val config2 = OAuthConfig(
            accessToken = "access",
            refreshToken = "refresh",
            clientId = "client-id",
            clientSecret = "client-secret",
        )
        assertEquals(config1, config2, "Two OAuthConfig instances with the same values must be equal")
        assertEquals(config1.hashCode(), config2.hashCode(), "Equal instances must have the same hashCode")

        val different = config1.copy(accessToken = "different-token")
        assertNotEquals(config1, different, "OAuthConfig instances with different values must not be equal")
    }
}
