package com.github.ayastrebov.volvo.api.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for RetryStrategy configuration class.
 */
class RetryStrategyTest {

    @Test
    fun defaultRetryStrategy_hasExpectedDefaults() {
        val strategy = RetryStrategy()
        assertEquals(3, strategy.maxRetries)
        assertEquals(2.0, strategy.base)
        assertEquals(60.seconds, strategy.maxDelay)
    }

    @Test
    fun retryStrategy_withCustomMaxRetries() {
        val strategy = RetryStrategy(maxRetries = 5)
        assertEquals(5, strategy.maxRetries)
        assertEquals(2.0, strategy.base)
        assertEquals(60.seconds, strategy.maxDelay)
    }

    @Test
    fun retryStrategy_withCustomBase() {
        val strategy = RetryStrategy(base = 3.0)
        assertEquals(3, strategy.maxRetries)
        assertEquals(3.0, strategy.base)
        assertEquals(60.seconds, strategy.maxDelay)
    }

    @Test
    fun retryStrategy_withCustomMaxDelay() {
        val strategy = RetryStrategy(maxDelay = 120.seconds)
        assertEquals(3, strategy.maxRetries)
        assertEquals(2.0, strategy.base)
        assertEquals(120.seconds, strategy.maxDelay)
    }

    @Test
    fun retryStrategy_withAllCustomValues() {
        val strategy = RetryStrategy(
            maxRetries = 10,
            base = 1.5,
            maxDelay = 30.seconds
        )
        assertEquals(10, strategy.maxRetries)
        assertEquals(1.5, strategy.base)
        assertEquals(30.seconds, strategy.maxDelay)
    }

    @Test
    fun retryStrategy_withZeroRetries() {
        val strategy = RetryStrategy(maxRetries = 0)
        assertEquals(0, strategy.maxRetries)
    }
}
