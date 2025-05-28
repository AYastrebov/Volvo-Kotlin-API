package com.github.ayastrebov.volvo.api.exception


/**
 * Represents an exception thrown when an error occurs while interacting with the OpenAI API.
 *
 * @property statusCode the HTTP status code associated with the error.
 * @property error an instance of [VolvoApiError] containing information about the error that occurred.
 */
public sealed class VolvoAPIException(
    public val statusCode: Int,
    public val error: VolvoApiError,
    throwable: Throwable? = null,
) : VolvoException(message = error.detail?.message, throwable = throwable)

/**
 * Represents an exception thrown when the OpenAI API rate limit is exceeded.
 */
public class RateLimitException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Represents an exception thrown when an invalid request is made to the OpenAI API.
 */
public class InvalidRequestException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Represents an exception thrown when an authentication error occurs while interacting with the OpenAI API.
 */
public class AuthenticationException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Represents an exception thrown when a permission error occurs while interacting with the OpenAI API.
 */
public class PermissionException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)

/**
 * Represents an exception thrown when an unknown error occurs while interacting with the OpenAI API.
 * This exception is used when the specific type of error is not covered by the existing subclasses.
 */
public class UnknownException(
    statusCode: Int,
    error: VolvoApiError,
    throwable: Throwable? = null
) : VolvoAPIException(statusCode, error, throwable)
