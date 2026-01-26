package com.github.ayastrebov.volvo.api.core

import com.github.ayastrebov.volvo.api.logging.HttpLogger
import com.github.ayastrebov.volvo.api.logging.LogLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for LoggingConfig configuration class.
 */
class LoggingConfigTest {

    @Test
    fun defaultLoggingConfig_hasExpectedDefaults() {
        val config = LoggingConfig()
        assertEquals(LogLevel.Headers, config.logLevel)
        assertNotNull(config.logger)
        assertTrue(config.sanitize)
    }

    @Test
    fun loggingConfig_withCustomLogLevel() {
        val config = LoggingConfig(logLevel = LogLevel.Body)
        assertEquals(LogLevel.Body, config.logLevel)
        assertNotNull(config.logger)
        assertTrue(config.sanitize)
    }

    @Test
    fun loggingConfig_withCustomLogger() {
        val customLogger = HttpLogger { }
        val config = LoggingConfig(logger = customLogger)
        assertEquals(LogLevel.Headers, config.logLevel)
        assertEquals(customLogger, config.logger)
        assertTrue(config.sanitize)
    }

    @Test
    fun loggingConfig_withSanitizeDisabled() {
        val config = LoggingConfig(sanitize = false)
        assertEquals(LogLevel.Headers, config.logLevel)
        assertNotNull(config.logger)
        assertEquals(false, config.sanitize)
    }

    @Test
    fun loggingConfig_withAllCustomValues() {
        val customLogger = HttpLogger { }
        val config = LoggingConfig(
            logLevel = LogLevel.All,
            logger = customLogger,
            sanitize = false
        )
        assertEquals(LogLevel.All, config.logLevel)
        assertEquals(customLogger, config.logger)
        assertEquals(false, config.sanitize)
    }

    @Test
    fun loggingConfig_logLevelNone_disablesLogging() {
        val config = LoggingConfig(logLevel = LogLevel.None)
        assertEquals(LogLevel.None, config.logLevel)
    }

    @Test
    fun loggingConfig_withNoOpLogger() {
        val config = LoggingConfig(logger = HttpLogger.NONE)
        assertEquals(HttpLogger.NONE, config.logger)
    }

    @Test
    fun loggingConfig_withSimpleLogger() {
        val config = LoggingConfig(logger = HttpLogger.SIMPLE)
        assertEquals(HttpLogger.SIMPLE, config.logger)
    }
}
