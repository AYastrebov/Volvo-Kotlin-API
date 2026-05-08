package com.github.ayastrebov.volvo.api.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * Tests for CircuitBreakerConfig validation and CircuitBreaker state machine behavior.
 */
class CircuitBreakerTest {

    // --- Config validation tests ---

    @Test
    fun config_defaultValues() {
        val config = CircuitBreakerConfig()
        assertEquals(5, config.failureThreshold)
        assertEquals(30.seconds, config.resetTimeout)
    }

    @Test
    fun config_negativeThreshold_throws() {
        val exception = assertFailsWith<IllegalArgumentException> {
            CircuitBreakerConfig(failureThreshold = -1)
        }
        assertTrue(exception.message!!.contains("failureThreshold must be positive"))
    }

    @Test
    fun config_zeroThreshold_throws() {
        val exception = assertFailsWith<IllegalArgumentException> {
            CircuitBreakerConfig(failureThreshold = 0)
        }
        assertTrue(exception.message!!.contains("failureThreshold must be positive"))
    }

    @Test
    fun config_negativeResetTimeout_throws() {
        val exception = assertFailsWith<IllegalArgumentException> {
            CircuitBreakerConfig(resetTimeout = (-1).seconds)
        }
        assertTrue(exception.message!!.contains("resetTimeout must be positive"))
    }

    // --- Circuit breaker behavior tests ---

    @Test
    fun closedState_allowsRequests() {
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 3))
        assertTrue(breaker.allowRequest(), "Closed circuit must allow requests")
    }

    @Test
    fun recordSuccess_resetsFailureCount() {
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 3))
        // Record 2 failures (below threshold)
        breaker.recordFailure()
        breaker.recordFailure()
        // Record success to reset
        breaker.recordSuccess()
        // Record 2 more failures -- should still be below threshold since count was reset
        breaker.recordFailure()
        breaker.recordFailure()
        assertTrue(breaker.allowRequest(),
            "Circuit must remain closed after success resets the failure count")
    }

    @Test
    fun recordFailure_belowThreshold_allowsRequests() {
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 5))
        repeat(4) { breaker.recordFailure() }
        assertTrue(breaker.allowRequest(),
            "Circuit must remain closed when failures are below threshold")
    }

    @Test
    fun recordFailure_atThreshold_opensCircuit() {
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 3))
        repeat(3) { breaker.recordFailure() }
        assertFalse(breaker.allowRequest(),
            "Circuit must be open after reaching failure threshold")
    }

    @Test
    fun openState_blocksRequests() {
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 1, resetTimeout = 10.seconds))
        breaker.recordFailure()
        assertFalse(breaker.allowRequest(), "Open circuit must block requests")
        assertFalse(breaker.allowRequest(), "Open circuit must continue blocking requests")
    }

    @Test
    fun openState_afterResetTimeout_allowsOneRequest() {
        // Use a very small reset timeout so it elapses nearly immediately
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 1, resetTimeout = 1.milliseconds))
        breaker.recordFailure()
        assertFalse(breaker.allowRequest(), "Circuit must be open immediately after failure")

        // Busy-wait until the reset timeout elapses using monotonic time source
        val mark = TimeSource.Monotonic.markNow()
        while (mark.elapsedNow() < 50.milliseconds) {
            if (breaker.allowRequest()) {
                // Circuit transitioned to half-open and allowed a request
                return
            }
        }
        // Final check after generous wait
        assertTrue(breaker.allowRequest(),
            "Circuit must transition to half-open after reset timeout elapses")
    }

    @Test
    fun halfOpenState_successCloses() {
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 1, resetTimeout = 1.milliseconds))
        breaker.recordFailure()

        // Wait for half-open transition
        val mark = TimeSource.Monotonic.markNow()
        while (mark.elapsedNow() < 50.milliseconds) {
            if (breaker.allowRequest()) break
        }

        // Record success in half-open state -- should close the circuit
        breaker.recordSuccess()
        assertTrue(breaker.allowRequest(), "Circuit must be closed after success in half-open state")
        assertTrue(breaker.allowRequest(), "Circuit must remain closed after recovery")
    }

    @Test
    fun halfOpenState_failureReopens() {
        val breaker = CircuitBreaker(CircuitBreakerConfig(failureThreshold = 1, resetTimeout = 1.milliseconds))
        breaker.recordFailure()

        // Wait for half-open transition
        val mark = TimeSource.Monotonic.markNow()
        while (mark.elapsedNow() < 50.milliseconds) {
            if (breaker.allowRequest()) break
        }

        // Record failure in half-open state -- should reopen the circuit
        breaker.recordFailure()
        assertFalse(breaker.allowRequest(), "Circuit must reopen after failure in half-open state")
    }
}
