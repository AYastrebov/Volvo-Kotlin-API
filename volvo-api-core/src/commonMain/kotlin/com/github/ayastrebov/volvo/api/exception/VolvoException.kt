package com.github.ayastrebov.volvo.api.exception

/**
 * Base exception for all Volvo API client errors.
 *
 * This is a sealed class hierarchy covering all error scenarios:
 * - [VolvoAPIException] — HTTP API errors (4xx status codes)
 * - [VolvoIOException] — Network and I/O errors (timeouts, connectivity)
 * - [VolvoHttpException] — Ktor HTTP client errors
 * - [VolvoServerException] — Server-side errors (5xx status codes)
 *
 * @see VolvoAPIException for typed API error handling
 */
public sealed class VolvoException(
    message: String? = null,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)

/**
 * Exception thrown when the Ktor HTTP client encounters a runtime error.
 *
 * This wraps unexpected Ktor client exceptions that are not related to
 * HTTP status codes or network I/O (e.g., serialization failures, protocol errors).
 */
public class VolvoHttpException(
    throwable: Throwable? = null,
) : VolvoException(throwable?.message, throwable)

/**
 * Exception thrown when the Volvo API returns a server error (HTTP 5xx).
 *
 * This indicates a problem on Volvo's servers, not with the request itself.
 * Retrying the request after a delay may resolve the issue.
 */
public class VolvoServerException(
    throwable: Throwable? = null,
) : VolvoException(message = throwable?.message, throwable = throwable)
