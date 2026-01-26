package com.github.ayastrebov.volvo.api.core

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Specifies the retry strategy for failed requests.
 *
 * Uses exponential backoff with configurable base and maximum delay.
 *
 * @property maxRetries The maximum number of retries to perform for a request (must be >= 0)
 * @property base The base value for exponential backoff calculation (must be > 0)
 * @property maxDelay The maximum delay between retry attempts (must be positive)
 * @property retryOnStatusCodes HTTP status codes that should trigger a retry (default: 429, 502, 503, 504)
 * @throws IllegalArgumentException if validation fails
 */
public data class RetryStrategy(
    public val maxRetries: Int = 3,
    public val base: Double = 2.0,
    public val maxDelay: Duration = 60.seconds,
    public val retryOnStatusCodes: Set<Int> = setOf(429, 502, 503, 504),
) {
    init {
        require(maxRetries >= 0) { "maxRetries must be non-negative, but was $maxRetries" }
        require(base > 0) { "base must be positive, but was $base" }
        require(maxDelay.isPositive()) { "maxDelay must be positive, but was $maxDelay" }
        require(retryOnStatusCodes.all { it in 400..599 }) {
            "retryOnStatusCodes must contain valid HTTP error codes (400-599), but was $retryOnStatusCodes"
        }
    }
}
