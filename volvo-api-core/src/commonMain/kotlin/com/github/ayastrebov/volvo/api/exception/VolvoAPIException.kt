package com.github.ayastrebov.volvo.api.exception


/**
 * Exception thrown when the Volvo API returns an HTTP error response.
 *
 * Subclasses map to specific HTTP status code ranges:
 * - [AuthenticationException] — 401 Unauthorized
 * - [PermissionException] — 403 Forbidden
 * - [RateLimitException] — 429 Too Many Requests
 * - [InvalidRequestException] — 400, 404, 409, 415
 * - [UnknownException] — Other 4xx status codes
 *
 * @property statusCode The HTTP status code returned by the API.
 * @property error The parsed error response body with code, message, and details.
 *
 * @see VolvoApiError for the error response structure
 */
public sealed class VolvoAPIException(
    public val statusCode: Int,
    public val error: VolvoApiError,
    throwable: Throwable? = null,
) : VolvoException(message = error.detail?.message, throwable = throwable)

/**
 * Exception thrown when the API rate limit is exceeded (HTTP 429).
 *
 * Standard rate limits: 100 requests/minute for status endpoints,
 * 10 requests/minute for command endpoints.
 * Consider using [com.github.ayastrebov.volvo.api.core.RetryStrategy] for automatic retry with backoff.
 */
public class RateLimitException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Exception thrown for invalid or malformed API requests (HTTP 400, 404, 409, 415).
 *
 * Common causes: invalid VIN, unsupported media type, resource conflict.
 */
public class InvalidRequestException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Exception thrown when the access token is invalid or expired (HTTP 401).
 *
 * Obtain a new OAuth2 access token via the Volvo ID identity system.
 */
public class AuthenticationException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Exception thrown when the API key lacks required permissions (HTTP 403).
 *
 * Verify your VCC API key has the necessary scopes in the
 * [Volvo Developer Portal](https://developer.volvocars.com/).
 */
public class PermissionException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Exception thrown for API errors not covered by other exception types.
 *
 * Check [statusCode] and [error] for details about the specific error.
 */
public class UnknownException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)
