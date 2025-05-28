package com.github.ayastrebov.volvo.api.exception

/** Volvo API client exception */
public sealed class VolvoException(
    message: String? = null,
    throwable: Throwable? = null
) : RuntimeException(message, throwable)

/** Runtime Http Client exception */
public class VolvoHttpException(
    throwable: Throwable? = null,
) : VolvoException(throwable?.message, throwable)

/** An exception thrown in case of a server error */
public class VolvoServerException(
    throwable: Throwable? = null,
) : VolvoException(message = throwable?.message, throwable = throwable)
