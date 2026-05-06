package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.test.TestData
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.core.ProxyConfig
import com.github.ayastrebov.volvo.api.core.RetryStrategy
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.logging.LogLevel
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for VolvoCarsConfig configuration options.
 */
class VolvoCarsConfigTest {

    // ==================== Basic Configuration ====================

    @Test
    fun config_withRequiredParameters_createsSuccessfully() {
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN
        )

        assertEquals(TestData.TEST_API_KEY, config.apiKey)
        assertEquals(TestData.TEST_TOKEN, config.token)
    }

    @Test
    fun config_hasDefaultValues() {
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN
        )

        // Default logging config
        assertNotNull(config.logging)
        assertEquals(LogLevel.Headers, config.logging.logLevel)
        assertTrue(config.logging.sanitize)

        // Default timeout
        assertNotNull(config.timeout)
        assertEquals(30.seconds, config.timeout.socket)

        // Default headers (empty)
        assertTrue(config.headers.isEmpty())

        // Default proxy (null)
        assertNull(config.proxy)

        // Default retry strategy
        assertNotNull(config.retry)
        assertEquals(3, config.retry.maxRetries)
        assertEquals(2.0, config.retry.base)
        assertEquals(60.seconds, config.retry.maxDelay)

        // Default engine (null - uses platform default)
        assertNull(config.engine)
    }

    // ==================== Timeout Configuration ====================

    @Test
    fun config_withCustomTimeout_setsTimeout() {
        val customTimeout = Timeout(
            socket = 60.seconds,
            connect = 10.seconds,
            request = 2.minutes
        )
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            timeout = customTimeout
        )

        assertEquals(60.seconds, config.timeout.socket)
        assertEquals(10.seconds, config.timeout.connect)
        assertEquals(2.minutes, config.timeout.request)
    }

    @Test
    fun config_withPartialTimeout_usesProvidedValues() {
        val customTimeout = Timeout(
            socket = 45.seconds
        )
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            timeout = customTimeout
        )

        assertEquals(45.seconds, config.timeout.socket)
        assertNull(config.timeout.connect)
        assertNull(config.timeout.request)
    }

    // ==================== Retry Configuration ====================

    @Test
    fun config_withCustomRetryStrategy_setsRetryParameters() {
        val customRetry = RetryStrategy(
            maxRetries = 5,
            base = 3.0,
            maxDelay = 120.seconds
        )
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            retry = customRetry
        )

        assertEquals(5, config.retry.maxRetries)
        assertEquals(3.0, config.retry.base)
        assertEquals(120.seconds, config.retry.maxDelay)
    }

    @Test
    fun config_withZeroRetries_disablesRetry() {
        val noRetry = RetryStrategy(maxRetries = 0)
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            retry = noRetry
        )

        assertEquals(0, config.retry.maxRetries)
    }

    // ==================== Headers Configuration ====================

    @Test
    fun config_withCustomHeaders_setsHeaders() {
        val customHeaders = mapOf(
            "X-Custom-Header" to "custom-value",
            "X-Another-Header" to "another-value"
        )
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            headers = customHeaders
        )

        assertEquals(2, config.headers.size)
        assertEquals("custom-value", config.headers["X-Custom-Header"])
        assertEquals("another-value", config.headers["X-Another-Header"])
    }

    // ==================== Proxy Configuration ====================

    @Test
    fun config_withHttpProxy_setsProxyConfig() {
        val httpProxy = ProxyConfig.Http("http://proxy.example.com:8080")
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            proxy = httpProxy
        )

        val proxy = config.proxy
        assertNotNull(proxy)
        assertIs<ProxyConfig.Http>(proxy)
        assertEquals("http://proxy.example.com:8080", proxy.url)
    }

    @Test
    fun config_withSocksProxy_setsProxyConfig() {
        val socksProxy = ProxyConfig.Socks("socks.example.com", 1080)
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            proxy = socksProxy
        )

        val proxy = config.proxy
        assertNotNull(proxy)
        assertIs<ProxyConfig.Socks>(proxy)
        assertEquals("socks.example.com", proxy.host)
        assertEquals(1080, proxy.port)
    }

    // ==================== Logging Configuration ====================

    @Test
    fun config_withCustomLogging_setsLoggingConfig() {
        val customLogging = LoggingConfig(
            logLevel = LogLevel.Body,
            sanitize = false
        )
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            logging = customLogging
        )

        assertEquals(LogLevel.Body, config.logging.logLevel)
        assertEquals(false, config.logging.sanitize)
    }

    @Test
    fun config_withLogLevelNone_disablesLogging() {
        val noLogging = LoggingConfig(logLevel = LogLevel.None)
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            logging = noLogging
        )

        assertEquals(LogLevel.None, config.logging.logLevel)
    }

    // ==================== Full Configuration ====================

    @Test
    fun config_withAllParameters_createsSuccessfully() {
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            logging = LoggingConfig(logLevel = LogLevel.All, sanitize = true),
            timeout = Timeout(socket = 45.seconds, connect = 15.seconds),
            headers = mapOf("X-Test" to "value"),
            proxy = ProxyConfig.Http("http://proxy:8080"),
            retry = RetryStrategy(maxRetries = 5, base = 2.5, maxDelay = 90.seconds)
        )

        assertEquals(TestData.TEST_API_KEY, config.apiKey)
        assertEquals(TestData.TEST_TOKEN, config.token)
        assertEquals(LogLevel.All, config.logging.logLevel)
        assertEquals(45.seconds, config.timeout.socket)
        assertEquals(15.seconds, config.timeout.connect)
        assertEquals("value", config.headers["X-Test"])
        assertTrue(config.proxy is ProxyConfig.Http)
        assertEquals(5, config.retry.maxRetries)
        assertEquals(2.5, config.retry.base)
        assertEquals(90.seconds, config.retry.maxDelay)
    }
}
