package com.github.ayastrebov.volvo.api.exception

/**
 * Exception thrown when a network or I/O error occurs during an API request.
 *
 * This is a sealed class with two subclasses:
 * - [VolvoTimeoutException] — Request, connect, or socket timeout exceeded
 * - [GenericIOException] — Other I/O errors (DNS resolution, connection refused, etc.)
 */
public sealed class VolvoIOException(
    throwable: Throwable? = null,
) : VolvoException(message = throwable?.message, throwable = throwable)

/**
 * Exception thrown when an HTTP request times out.
 *
 * This can occur due to request timeout, connect timeout, or socket timeout
 * as configured in [com.github.ayastrebov.volvo.api.http.Timeout].
 */
public class VolvoTimeoutException(
    throwable: Throwable
) : VolvoIOException(throwable = throwable)

/**
 * Exception thrown for network I/O errors other than timeouts.
 *
 * Examples: DNS resolution failure, connection refused, connection reset.
 */
public class GenericIOException(
    throwable: Throwable? = null,
) : VolvoIOException(throwable = throwable)
