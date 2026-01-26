package com.github.ayastrebov.volvo.api.logging

/**
 * Interface for HTTP client logging.
 *
 * This is a simple logging interface that allows plugging in any logging framework.
 * The library uses this for HTTP request/response logging.
 *
 * ## Usage with Popular KMP Loggers
 *
 * ### Napier
 * ```kotlin
 * val logger = object : HttpLogger {
 *     override fun log(message: String) {
 *         Napier.d(message, tag = "VolvoAPI")
 *     }
 * }
 * ```
 *
 * ### Kermit
 * ```kotlin
 * val logger = object : HttpLogger {
 *     override fun log(message: String) {
 *         Logger.d("VolvoAPI") { message }
 *     }
 * }
 * ```
 *
 * ### kotlin-logging (JVM)
 * ```kotlin
 * val kLogger = KotlinLogging.logger {}
 * val logger = object : HttpLogger {
 *     override fun log(message: String) {
 *         kLogger.debug { message }
 *     }
 * }
 * ```
 *
 * ### Disable logging
 * ```kotlin
 * val logger = HttpLogger.NONE
 * ```
 */
public fun interface HttpLogger {
    /**
     * Logs a message.
     *
     * @param message The message to log.
     */
    public fun log(message: String)

    public companion object {
        /**
         * Simple logger that writes to standard output using `println`.
         */
        public val SIMPLE: HttpLogger = HttpLogger { println(it) }

        /**
         * No-operation logger that discards all messages.
         */
        public val NONE: HttpLogger = HttpLogger { }
    }
}
