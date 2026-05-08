package com.github.ayastrebov.volvo.api.core

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Tests for PKCE (Proof Key for Code Exchange) generation.
 */
class PkceTest {

    @Test
    fun generate_producesNonEmptyVerifierAndChallenge() {
        val pkce = Pkce.generate()
        assertTrue(pkce.codeVerifier.isNotEmpty(), "codeVerifier must not be empty")
        assertTrue(pkce.codeChallenge.isNotEmpty(), "codeChallenge must not be empty")
    }

    @Test
    fun generate_verifierIsUrlSafeBase64() {
        val pkce = Pkce.generate()
        val verifier = pkce.codeVerifier
        // URL-safe base64 must not contain +, /, or = characters
        assertTrue('+' !in verifier, "codeVerifier must not contain '+'")
        assertTrue('/' !in verifier, "codeVerifier must not contain '/'")
        assertTrue('=' !in verifier, "codeVerifier must not contain '='")
        // Should only contain alphanumeric, hyphen, and underscore
        assertTrue(verifier.all { it.isLetterOrDigit() || it == '-' || it == '_' },
            "codeVerifier must only contain URL-safe base64 characters, but was: $verifier")
    }

    @Test
    fun generate_challengeIsDifferentFromVerifier() {
        val pkce = Pkce.generate()
        assertNotEquals(pkce.codeVerifier, pkce.codeChallenge,
            "codeChallenge must differ from codeVerifier (it is the SHA-256 hash)")
    }

    @Test
    fun generate_codeChallengeMethodIsS256() {
        val pkce = Pkce.generate()
        assertEquals("S256", pkce.codeChallengeMethod)
    }

    @Test
    fun generate_twoCalls_produceDifferentValues() {
        val first = Pkce.generate()
        val second = Pkce.generate()
        assertNotEquals(first.codeVerifier, second.codeVerifier,
            "Two generate() calls must produce different verifiers")
        assertNotEquals(first.codeChallenge, second.codeChallenge,
            "Two generate() calls must produce different challenges")
    }

    @Test
    fun generate_withFixedRandom_producesReproducibleOutput() {
        val seed = 42
        val first = Pkce.generate(random = Random(seed))
        val second = Pkce.generate(random = Random(seed))
        assertEquals(first.codeVerifier, second.codeVerifier,
            "Same random seed must produce the same verifier")
        assertEquals(first.codeChallenge, second.codeChallenge,
            "Same random seed must produce the same challenge")
    }

    @Test
    fun generate_verifierLengthIsReasonable() {
        val pkce = Pkce.generate()
        // RFC 7636 requires 43-128 characters; 32 random bytes base64url-encoded yields 43 characters
        assertTrue(pkce.codeVerifier.length >= 32,
            "codeVerifier must be at least 32 characters, but was ${pkce.codeVerifier.length}")
    }
}
