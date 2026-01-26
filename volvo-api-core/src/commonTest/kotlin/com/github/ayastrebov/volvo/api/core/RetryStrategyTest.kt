package com.github.ayastrebov.volvo.api.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for RetryStrategy configuration class.
 */
class RetryStrategyTest {

    private val defaultStatusCodes = setOf(429, 502, 503, 504)

    @Test
    fun defaultRetryStrategy_hasExpectedDefaults() {
        val strategy = RetryStrategy()
        assertEquals(3, strategy.maxRetries)
        assertEquals(2.0, strategy.base)
        assertEquals(60.seconds, strategy.maxDelay)
        assertEquals(defaultStatusCodes, strategy.retryOnStatusCodes)
    }

    @Test
    fun retryStrategy_withCustomMaxRetries() {
        val strategy = RetryStrategy(maxRetries = 5)
        assertEquals(5, strategy.maxRetries)
        assertEquals(2.0, strategy.base)
        assertEquals(60.seconds, strategy.maxDelay)
        assertEquals(defaultStatusCodes, strategy.retryOnStatusCodes)
    }

    @Test
    fun retryStrategy_withCustomBase() {
        val strategy = RetryStrategy(base = 3.0)
        assertEquals(3, strategy.maxRetries)
        assertEquals(3.0, strategy.base)
        assertEquals(60.seconds, strategy.maxDelay)
        assertEquals(defaultStatusCodes, strategy.retryOnStatusCodes)
    }

    @Test
    fun retryStrategy_withCustomMaxDelay() {
        val strategy = RetryStrategy(maxDelay = 120.seconds)
        assertEquals(3, strategy.maxRetries)
        assertEquals(2.0, strategy.base)
        assertEquals(120.seconds, strategy.maxDelay)
        assertEquals(defaultStatusCodes, strategy.retryOnStatusCodes)
    }

    @Test
    fun retryStrategy_withCustomStatusCodes() {
        val customCodes = setOf(429, 500, 502)
        val strategy = RetryStrategy(retryOnStatusCodes = customCodes)
        assertEquals(3, strategy.maxRetries)
        assertEquals(customCodes, strategy.retryOnStatusCodes)
    }

    @Test
    fun retryStrategy_withAllCustomValues() {
        val customCodes = setOf(429, 503)
        val strategy = RetryStrategy(
            maxRetries = 10,
            base = 1.5,
            maxDelay = 30.seconds,
            retryOnStatusCodes = customCodes
        )
        assertEquals(10, strategy.maxRetries)
        assertEquals(1.5, strategy.base)
        assertEquals(30.seconds, strategy.maxDelay)
        assertEquals(customCodes, strategy.retryOnStatusCodes)
    }

    @Test
    fun retryStrategy_withZeroRetries() {
        val strategy = RetryStrategy(maxRetries = 0)
        assertEquals(0, strategy.maxRetries)
    }

    // Validation tests

    @Test
    fun retryStrategy_negativeMaxRetries_throwsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            RetryStrategy(maxRetries = -1)
        }
        assertTrue(exception.message!!.contains("maxRetries must be non-negative"))
    }

    @Test
    fun retryStrategy_zeroBase_throwsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            RetryStrategy(base = 0.0)
        }
        assertTrue(exception.message!!.contains("base must be positive"))
    }

    @Test
    fun retryStrategy_negativeBase_throwsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            RetryStrategy(base = -1.0)
        }
        assertTrue(exception.message!!.contains("base must be positive"))
    }

    @Test
    fun retryStrategy_zeroMaxDelay_throwsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            RetryStrategy(maxDelay = 0.seconds)
        }
        assertTrue(exception.message!!.contains("maxDelay must be positive"))
    }

    @Test
    fun retryStrategy_negativeMaxDelay_throwsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            RetryStrategy(maxDelay = (-1).seconds)
        }
        assertTrue(exception.message!!.contains("maxDelay must be positive"))
    }

    @Test
    fun retryStrategy_invalidStatusCode_throwsException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            RetryStrategy(retryOnStatusCodes = setOf(200))
        }
        assertTrue(exception.message!!.contains("retryOnStatusCodes must contain valid HTTP error codes"))
    }

    @Test
    fun retryStrategy_emptyStatusCodes_isValid() {
        // Empty set is valid - it means no retries on status codes
        val strategy = RetryStrategy(retryOnStatusCodes = emptySet())
        assertEquals(emptySet(), strategy.retryOnStatusCodes)
    }
}
