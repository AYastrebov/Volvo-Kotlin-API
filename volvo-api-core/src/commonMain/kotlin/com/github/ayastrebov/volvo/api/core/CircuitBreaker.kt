package com.github.ayastrebov.volvo.api.core

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * Circuit breaker that stops retries when failures exceed a threshold.
 *
 * Prevents cascading failures by "opening" the circuit after [failureThreshold]
 * consecutive failures within [resetTimeout]. While open, requests fail immediately
 * with [com.github.ayastrebov.volvo.api.exception.RateLimitException] instead of
 * hitting the server.
 *
 * After [resetTimeout] elapses, the circuit moves to "half-open" and allows one
 * request through. If it succeeds, the circuit closes. If it fails, it opens again.
 *
 * ```kotlin
 * VolvoCarsConfig(
 *     apiKey = "your-key",
 *     token = "your-token",
 *     circuitBreaker = CircuitBreakerConfig(
 *         failureThreshold = 5,
 *         resetTimeout = 60.seconds
 *     )
 * )
 * ```
 *
 * @property failureThreshold Number of consecutive failures before opening the circuit (must be > 0)
 * @property resetTimeout Duration to wait before allowing a test request through (must be positive)
 */
public data class CircuitBreakerConfig(
    public val failureThreshold: Int = 5,
    public val resetTimeout: Duration = 30.seconds,
) {
    init {
        require(failureThreshold > 0) { "failureThreshold must be positive, but was $failureThreshold" }
        require(resetTimeout.isPositive()) { "resetTimeout must be positive, but was $resetTimeout" }
    }
}

/**
 * Circuit breaker state machine.
 *
 * Thread-safe for concurrent coroutine access via `@Synchronized`-like atomic operations.
 */
public class CircuitBreaker public constructor(private val config: CircuitBreakerConfig) {
    private val timeSource = TimeSource.Monotonic

    private var state: State = State.Closed

    private var consecutiveFailures: Int = 0

    private var openedAt: TimeSource.Monotonic.ValueTimeMark? = null

    private enum class State { Closed, Open, HalfOpen }

    /**
     * Checks if a request should be allowed through.
     *
     * @return `true` if the request is allowed, `false` if the circuit is open
     */
    public fun allowRequest(): Boolean = when (state) {
        State.Closed -> true
        State.Open -> {
            val mark = openedAt
            if (mark != null && mark.elapsedNow() >= config.resetTimeout) {
                state = State.HalfOpen
                true
            } else {
                false
            }
        }
        State.HalfOpen -> true
    }

    /**
     * Records a successful request. Resets the circuit to closed.
     */
    public fun recordSuccess() {
        consecutiveFailures = 0
        state = State.Closed
    }

    /**
     * Records a failed request. Opens the circuit if threshold is exceeded.
     */
    public fun recordFailure() {
        consecutiveFailures++
        if (consecutiveFailures >= config.failureThreshold) {
            state = State.Open
            openedAt = timeSource.markNow()
        }
    }
}
