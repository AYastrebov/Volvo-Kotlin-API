package com.github.ayastrebov.volvo.api.logging

/**
 * HTTP client logging verbosity level.
 *
 * Controls which aspects of HTTP requests and responses are logged.
 * Levels are ordered from most verbose ([All]) to least verbose ([None]).
 */
public enum class LogLevel {
    /** Log everything: request/response headers, bodies, and metadata. */
    All,

    /** Log HTTP headers only, without request/response bodies. */
    Headers,

    /** Log request/response bodies only, without headers. */
    Body,

    /** Log basic request/response information (method, URL, status code). */
    Info,

    /** Disable all HTTP logging. */
    None
}
