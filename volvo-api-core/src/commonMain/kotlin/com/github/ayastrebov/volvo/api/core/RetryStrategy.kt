package com.github.ayastrebov.volvo.api.core

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Specifies the retry strategy for failed requests.
 *
 * Uses exponential backoff with configurable base and maximum delay.
 *
 * @property maxRetries The maximum number of retries to perform for a request
 * @property base The base value for exponential backoff calculation
 * @property maxDelay The maximum delay between retry attempts
 */
public data class RetryStrategy(
    public val maxRetries: Int = 3,
    public val base: Double = 2.0,
    public val maxDelay: Duration = 60.seconds,
)
