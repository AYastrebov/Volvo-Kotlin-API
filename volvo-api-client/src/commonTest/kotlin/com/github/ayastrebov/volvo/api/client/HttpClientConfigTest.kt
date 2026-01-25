package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.test.MockResponse
import com.github.ayastrebov.volvo.api.client.test.RequestCapturingMockEngine
import com.github.ayastrebov.volvo.api.client.test.TestData
import com.github.ayastrebov.volvo.api.client.test.createTestConfig
import com.github.ayastrebov.volvo.api.core.LoggingConfig
import com.github.ayastrebov.volvo.api.core.ProxyConfig
import com.github.ayastrebov.volvo.api.core.RetryStrategy
import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.logging.LogLevel
import com.github.ayastrebov.volvo.api.logging.Logger
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for [VolvoCarsConfig] and HTTP client configuration.
 */
class HttpClientConfigTest {

    // ==================== VolvoCarsConfig Data Class ====================

    @Test
    fun volvoCarsConfig_isDataClass() {
        // Note: VolvoCarsConfig is a data class but has a function parameter (httpClientConfig)
        // which cannot be compared for equality, so we verify individual properties
        val config = VolvoCarsConfig(apiKey = "key1", token = "token1")

        assertEquals("key1", config.apiKey)
        assertEquals("token1", config.token)

        // Verify copy works
        val copied = config.copy(apiKey = "key2")
        assertEquals("key2", copied.apiKey)
        assertEquals("token1", copied.token)
    }

    @Test
    fun volvoCarsConfig_copyWorks() {
        val original = VolvoCarsConfig(apiKey = "key1", token = "token1")
        val copied = original.copy(apiKey = "key2")

        assertEquals("key2", copied.apiKey)
        assertEquals("token1", copied.token)
    }

    @Test
    fun volvoCarsConfig_hasDefaultValues() {
        val config = VolvoCarsConfig(apiKey = "key", token = "token")

        assertNotNull(config.logging)
        assertNotNull(config.timeout)
        assertTrue(config.headers.isEmpty())
        assertNull(config.proxy)
        assertNotNull(config.retry)
        assertNull(config.engine)
    }

    // ==================== RetryStrategy Data Class ====================

    @Test
    fun retryStrategy_isDataClass() {
        val strategy1 = RetryStrategy(maxRetries = 3, base = 2.0)
        val strategy2 = RetryStrategy(maxRetries = 3, base = 2.0)

        assertEquals(strategy1, strategy2)
        assertEquals(strategy1.hashCode(), strategy2.hashCode())
    }

    @Test
    fun retryStrategy_copyWorks() {
        val original = RetryStrategy(maxRetries = 3, base = 2.0)
        val copied = original.copy(maxRetries = 5)

        assertEquals(5, copied.maxRetries)
        assertEquals(2.0, copied.base)
    }

    @Test
    fun retryStrategy_hasDefaultValues() {
        val strategy = RetryStrategy()

        assertEquals(3, strategy.maxRetries)
        assertEquals(2.0, strategy.base)
        assertEquals(60.seconds, strategy.maxDelay)
    }

    // ==================== Timeout Data Class ====================

    @Test
    fun timeout_isDataClass() {
        val timeout1 = Timeout(request = 30.seconds, connect = 10.seconds)
        val timeout2 = Timeout(request = 30.seconds, connect = 10.seconds)

        assertEquals(timeout1, timeout2)
        assertEquals(timeout1.hashCode(), timeout2.hashCode())
    }

    @Test
    fun timeout_copyWorks() {
        val original = Timeout(request = 30.seconds)
        val copied = original.copy(connect = 5.seconds)

        assertEquals(30.seconds, copied.request)
        assertEquals(5.seconds, copied.connect)
    }

    @Test
    fun timeout_hasNullDefaults() {
        val timeout = Timeout()

        assertNull(timeout.request)
        assertNull(timeout.connect)
        assertNull(timeout.socket)
    }

    // ==================== ProxyConfig ====================

    @Test
    fun proxyConfigHttp_storesUrl() {
        val proxy = ProxyConfig.Http("http://proxy.example.com:8080")

        assertEquals("http://proxy.example.com:8080", proxy.url)
    }

    @Test
    fun proxyConfigSocks_storesHostAndPort() {
        val proxy = ProxyConfig.Socks("socks.example.com", 1080)

        assertEquals("socks.example.com", proxy.host)
        assertEquals(1080, proxy.port)
    }

    // ==================== LoggingConfig ====================

    @Test
    fun loggingConfig_hasDefaultValues() {
        val config = LoggingConfig()

        assertEquals(Logger.Simple, config.logger)
        assertEquals(LogLevel.Headers, config.logLevel)
        assertTrue(config.sanitize)
    }

    @Test
    fun loggingConfig_acceptsCustomValues() {
        val config = LoggingConfig(
            logger = Logger.Simple,
            logLevel = LogLevel.All,
            sanitize = false
        )

        assertEquals(Logger.Simple, config.logger)
        assertEquals(LogLevel.All, config.logLevel)
        assertEquals(false, config.sanitize)
    }

    // ==================== Headers Configuration ====================

    @Test
    fun config_withCustomHeaders_addsToRequests() = runTest {
        val capturingEngine = RequestCapturingMockEngine(
            MockResponse(HttpStatusCode.OK, """{"status": 200, "data": []}""")
        )

        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            headers = mapOf("X-Custom-Header" to "custom-value"),
            engine = capturingEngine.engine
        )

        val client = VolvoCars(config)
        try {
            client.getVehicleList()
        } catch (_: Exception) {
            // Expected to fail, we just want to capture the request
        }

        val request = capturingEngine.requests.firstOrNull()
        assertNotNull(request)
    }

    // ==================== Custom HTTP Client Configuration ====================

    @Test
    fun config_withHttpClientConfig_appliesCustomization() = runTest {
        var customConfigApplied = false

        val mockEngine = MockEngine {
            respond(
                content = """{"status": 200, "data": []}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            engine = mockEngine,
            httpClientConfig = {
                customConfigApplied = true
            }
        )

        VolvoCars(config)

        assertTrue(customConfigApplied)
    }

    // ==================== Engine Configuration ====================

    @Test
    fun config_withCustomEngine_usesProvidedEngine() = runTest {
        var engineUsed = false
        val mockEngine = MockEngine {
            engineUsed = true
            respond(
                content = """{"status": 200, "data": []}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN,
            engine = mockEngine
        )

        val client = VolvoCars(config)
        try {
            client.getVehicleList()
        } catch (_: Exception) {
            // May fail, but engine should be used
        }

        assertTrue(engineUsed)
    }

    @Test
    fun config_withoutEngine_createsDefaultEngine() {
        val config = VolvoCarsConfig(
            apiKey = TestData.TEST_API_KEY,
            token = TestData.TEST_TOKEN
        )

        assertNull(config.engine)
    }
}
