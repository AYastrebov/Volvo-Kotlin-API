package com.github.ayastrebov.volvo.api.exception

/** An exception thrown in case of an I/O error */
public sealed class VolvoIOException(
    throwable: Throwable? = null,
) : VolvoException(message = throwable?.message, throwable = throwable)

/** An exception thrown in case a request times out. */
public class VolvoTimeoutException(
    throwable: Throwable
) : VolvoIOException(throwable = throwable)

/** An exception thrown in case of an I/O error */
public class GenericIOException(
    throwable: Throwable? = null,
) : VolvoIOException(throwable = throwable)
