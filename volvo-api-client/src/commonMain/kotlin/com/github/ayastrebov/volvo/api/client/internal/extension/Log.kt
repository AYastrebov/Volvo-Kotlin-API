package com.github.ayastrebov.volvo.api.client.internal.extension

import com.github.ayastrebov.volvo.api.logging.HttpLogger
import com.github.ayastrebov.volvo.api.logging.LogLevel
import io.ktor.client.plugins.logging.LogLevel as KLogLevel
import io.ktor.client.plugins.logging.Logger as KLogger

/**
 * Convert [HttpLogger] to Ktor's Logger.
 */
internal fun HttpLogger.toKtorLogger(): KLogger = object : KLogger {
    override fun log(message: String) {
        this@toKtorLogger.log(message)
    }
}

/**
 * Convert LogLevel to Ktor's LogLevel.
 */
internal fun LogLevel.toKtorLogLevel() = when (this) {
    LogLevel.All -> KLogLevel.ALL
    LogLevel.Headers -> KLogLevel.HEADERS
    LogLevel.Body -> KLogLevel.BODY
    LogLevel.Info -> KLogLevel.INFO
    LogLevel.None -> KLogLevel.NONE
}
