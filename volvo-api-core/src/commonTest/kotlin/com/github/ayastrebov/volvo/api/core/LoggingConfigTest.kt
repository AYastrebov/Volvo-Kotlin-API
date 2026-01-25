package com.github.ayastrebov.volvo.api.core

import com.github.ayastrebov.volvo.api.logging.LogLevel
import com.github.ayastrebov.volvo.api.logging.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for LoggingConfig configuration class.
 */
class LoggingConfigTest {

    @Test
    fun defaultLoggingConfig_hasExpectedDefaults() {
        val config = LoggingConfig()
        assertEquals(LogLevel.Headers, config.logLevel)
        assertEquals(Logger.Simple, config.logger)
        assertTrue(config.sanitize)
    }

    @Test
    fun loggingConfig_withCustomLogLevel() {
        val config = LoggingConfig(logLevel = LogLevel.Body)
        assertEquals(LogLevel.Body, config.logLevel)
        assertEquals(Logger.Simple, config.logger)
        assertTrue(config.sanitize)
    }

    @Test
    fun loggingConfig_withCustomLogger() {
        val config = LoggingConfig(logger = Logger.Default)
        assertEquals(LogLevel.Headers, config.logLevel)
        assertEquals(Logger.Default, config.logger)
        assertTrue(config.sanitize)
    }

    @Test
    fun loggingConfig_withSanitizeDisabled() {
        val config = LoggingConfig(sanitize = false)
        assertEquals(LogLevel.Headers, config.logLevel)
        assertEquals(Logger.Simple, config.logger)
        assertEquals(false, config.sanitize)
    }

    @Test
    fun loggingConfig_withAllCustomValues() {
        val config = LoggingConfig(
            logLevel = LogLevel.All,
            logger = Logger.Empty,
            sanitize = false
        )
        assertEquals(LogLevel.All, config.logLevel)
        assertEquals(Logger.Empty, config.logger)
        assertEquals(false, config.sanitize)
    }

    @Test
    fun loggingConfig_logLevelNone_disablesLogging() {
        val config = LoggingConfig(logLevel = LogLevel.None)
        assertEquals(LogLevel.None, config.logLevel)
    }
}
