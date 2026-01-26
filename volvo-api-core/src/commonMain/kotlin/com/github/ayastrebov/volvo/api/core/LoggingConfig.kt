package com.github.ayastrebov.volvo.api.core

import com.github.ayastrebov.volvo.api.logging.HttpLogger
import com.github.ayastrebov.volvo.api.logging.LogLevel

/**
 * Configuration for HTTP client logging.
 *
 * ## Using Built-in Loggers
 *
 * ```kotlin
 * // Simple println logger (default)
 * LoggingConfig()
 *
 * // Disable logging
 * LoggingConfig(logger = HttpLogger.NONE)
 * ```
 *
 * ## Using Custom Loggers
 *
 * Plug in any logging framework:
 *
 * ```kotlin
 * // Napier
 * LoggingConfig(logger = HttpLogger { Napier.d(it, tag = "VolvoAPI") })
 *
 * // Kermit
 * LoggingConfig(logger = HttpLogger { Logger.d("VolvoAPI") { it } })
 *
 * // kotlin-logging (JVM)
 * val kLogger = KotlinLogging.logger {}
 * LoggingConfig(logger = HttpLogger { kLogger.debug { it } })
 * ```
 *
 * @property logLevel The level of detail for HTTP logging.
 * @property logger The logger implementation to use.
 * @property sanitize Whether to sanitize sensitive headers (e.g., Authorization) in logs.
 */
public data class LoggingConfig(
    public val logLevel: LogLevel = LogLevel.Headers,
    public val logger: HttpLogger = HttpLogger.SIMPLE,
    public val sanitize: Boolean = true,
)
