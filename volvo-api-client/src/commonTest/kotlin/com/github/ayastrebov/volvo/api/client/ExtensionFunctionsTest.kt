@file:OptIn(InternalVolvoApi::class)

package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.InternalVolvoApi
import com.github.ayastrebov.volvo.api.client.internal.extension.requestOptions
import com.github.ayastrebov.volvo.api.client.internal.extension.toKtorLogLevel
import com.github.ayastrebov.volvo.api.client.internal.extension.toKtorLogger
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.logging.HttpLogger
import com.github.ayastrebov.volvo.api.logging.LogLevel
import io.ktor.client.request.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger

/**
 * Tests for extension functions in the internal.extension package.
 */
class ExtensionFunctionsTest {

    // ==================== HttpLogger Conversion ====================

    @Test
    fun httpLoggerSimple_convertsToKtorLogger() {
        val result = HttpLogger.SIMPLE.toKtorLogger()
        assertTrue(result is KtorLogger)
    }

    @Test
    fun httpLoggerNone_convertsToKtorLogger() {
        val result = HttpLogger.NONE.toKtorLogger()
        assertTrue(result is KtorLogger)
    }

    @Test
    fun customHttpLogger_convertsToKtorLogger() {
        val messages = mutableListOf<String>()
        val customLogger = HttpLogger { messages.add(it) }

        val ktorLogger = customLogger.toKtorLogger()
        ktorLogger.log("Test message")

        assertEquals(1, messages.size)
        assertEquals("Test message", messages.first())
    }

    // ==================== LogLevel Conversion ====================

    @Test
    fun logLevelAll_convertsToKtorAll() {
        val result = LogLevel.All.toKtorLogLevel()
        assertEquals(KtorLogLevel.ALL, result)
    }

    @Test
    fun logLevelHeaders_convertsToKtorHeaders() {
        val result = LogLevel.Headers.toKtorLogLevel()
        assertEquals(KtorLogLevel.HEADERS, result)
    }

    @Test
    fun logLevelBody_convertsToKtorBody() {
        val result = LogLevel.Body.toKtorLogLevel()
        assertEquals(KtorLogLevel.BODY, result)
    }

    @Test
    fun logLevelInfo_convertsToKtorInfo() {
        val result = LogLevel.Info.toKtorLogLevel()
        assertEquals(KtorLogLevel.INFO, result)
    }

    @Test
    fun logLevelNone_convertsToKtorNone() {
        val result = LogLevel.None.toKtorLogLevel()
        assertEquals(KtorLogLevel.NONE, result)
    }

    // ==================== RequestOptions Extension ====================

    @Test
    fun requestOptions_withNullOptions_doesNothing() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com")

        builder.requestOptions(null)

        // Builder should remain unchanged (no crash)
        assertTrue(builder.headers.isEmpty())
    }

    @Test
    fun requestOptions_appliesHeaders() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com")
        val options = RequestOptions(
            headers = mapOf("X-Custom-Header" to "value1", "X-Another" to "value2")
        )

        builder.requestOptions(options)

        assertEquals("value1", builder.headers["X-Custom-Header"])
        assertEquals("value2", builder.headers["X-Another"])
    }

    @Test
    fun requestOptions_overridesExistingHeaders() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com")
        builder.headers.append("X-Existing", "old-value")
        val options = RequestOptions(
            headers = mapOf("X-Existing" to "new-value")
        )

        builder.requestOptions(options)

        assertEquals("new-value", builder.headers["X-Existing"])
    }

    @Test
    fun requestOptions_appliesUrlParameters() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com/path")
        val options = RequestOptions(
            urlParameters = mapOf("page" to "1", "limit" to "10")
        )

        builder.requestOptions(options)

        assertEquals("1", builder.url.parameters["page"])
        assertEquals("10", builder.url.parameters["limit"])
    }

    @Test
    fun requestOptions_appliesTimeouts() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com")
        val options = RequestOptions(
            timeout = Timeout(
                connect = 5.seconds,
                request = 30.seconds,
                socket = 10.seconds
            )
        )

        builder.requestOptions(options)

        // The timeout is applied to the builder's attributes
        // We can't easily verify internal timeout values, but this ensures no crash
    }

    @Test
    fun requestOptions_withPartialTimeout_appliesOnlySetValues() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com")
        val options = RequestOptions(
            timeout = Timeout(
                connect = 5.seconds,
                request = null,
                socket = null
            )
        )

        builder.requestOptions(options)

        // Should not crash when only some timeout values are set
    }

    @Test
    fun requestOptions_withEmptyOptions_doesNotModifyBuilder() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com")
        val initialHeadersCount = builder.headers.entries().size
        val options = RequestOptions()

        builder.requestOptions(options)

        assertEquals(initialHeadersCount, builder.headers.entries().size)
    }

    @Test
    fun requestOptions_combinesAllSettings() {
        val builder = HttpRequestBuilder()
        builder.url("https://api.test.com")
        val options = RequestOptions(
            headers = mapOf("Authorization" to "Bearer token"),
            urlParameters = mapOf("vin" to "YV1XZ00ABC1234567"),
            timeout = Timeout(request = 60.seconds)
        )

        builder.requestOptions(options)

        assertEquals("Bearer token", builder.headers["Authorization"])
        assertEquals("YV1XZ00ABC1234567", builder.url.parameters["vin"])
    }

    // ==================== RequestOptions Data Class ====================

    @Test
    fun requestOptions_isDataClass() {
        val options1 = RequestOptions(headers = mapOf("key" to "value"))
        val options2 = RequestOptions(headers = mapOf("key" to "value"))

        assertEquals(options1, options2)
        assertEquals(options1.hashCode(), options2.hashCode())
    }

    @Test
    fun requestOptions_copyWorks() {
        val original = RequestOptions(
            headers = mapOf("key" to "value"),
            urlParameters = mapOf("param" to "1")
        )

        val copied = original.copy(headers = mapOf("newKey" to "newValue"))

        assertEquals(mapOf("newKey" to "newValue"), copied.headers)
        assertEquals(mapOf("param" to "1"), copied.urlParameters)
    }

    // ==================== LoggingConfig ====================

    @Test
    fun loggingConfig_defaultLogger() {
        val config = LoggingConfig()
        assertNotNull(config.logger)
    }

    @Test
    fun loggingConfig_customLogger() {
        val messages = mutableListOf<String>()
        val customLogger = HttpLogger { messages.add(it) }
        val config = LoggingConfig(logger = customLogger)

        config.logger.log("test")
        assertEquals(listOf("test"), messages)
    }

    @Test
    fun loggingConfig_noOpLogger() {
        val config = LoggingConfig(logger = HttpLogger.NONE)
        // Should not throw
        config.logger.log("ignored")
    }
}
