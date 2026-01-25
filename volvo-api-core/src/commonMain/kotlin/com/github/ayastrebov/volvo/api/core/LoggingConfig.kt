package com.github.ayastrebov.volvo.api.core

import com.github.ayastrebov.volvo.api.logging.LogLevel
import com.github.ayastrebov.volvo.api.logging.Logger

/**
 * Configuration for HTTP client logging.
 *
 * @property logLevel The level of logging to be used by the HTTP client
 * @property logger The logger instance to be used by the HTTP client
 * @property sanitize Flag indicating whether to sanitize sensitive information
 *                   (e.g., authorization headers) in the logs
 */
public data class LoggingConfig(
    public val logLevel: LogLevel = LogLevel.Headers,
    public val logger: Logger = Logger.Simple,
    public val sanitize: Boolean = true,
)
