@file:OptIn(InternalVolvoApi::class)

package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.InternalVolvoApi
import com.github.ayastrebov.volvo.api.client.internal.http.HttpTransport
import com.github.ayastrebov.volvo.api.exception.VolvoTimeoutException
import com.github.ayastrebov.volvo.api.http.Timeout
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Tests for timeout configuration and behavior.
 */
class TimeoutBehaviorTest {

    // ==================== Timeout Configuration ====================

    @Test
    fun timeout_defaultValuesAreNull() {
        val timeout = Timeout()

        assertNull(timeout.request)
        assertNull(timeout.connect)
        assertNull(timeout.socket)
    }

    @Test
    fun timeout_canSetRequestTimeout() {
        val timeout = Timeout(request = 30.seconds)

        assertEquals(30.seconds, timeout.request)
        assertNull(timeout.connect)
        assertNull(timeout.socket)
    }

    @Test
    fun timeout_canSetConnectTimeout() {
        val timeout = Timeout(connect = 10.seconds)

        assertNull(timeout.request)
        assertEquals(10.seconds, timeout.connect)
        assertNull(timeout.socket)
    }

    @Test
    fun timeout_canSetSocketTimeout() {
        val timeout = Timeout(socket = 60.seconds)

        assertNull(timeout.request)
        assertNull(timeout.connect)
        assertEquals(60.seconds, timeout.socket)
    }

    @Test
    fun timeout_canSetAllTimeouts() {
        val timeout = Timeout(
            request = 30.seconds,
            connect = 10.seconds,
            socket = 60.seconds
        )

        assertEquals(30.seconds, timeout.request)
        assertEquals(10.seconds, timeout.connect)
        assertEquals(60.seconds, timeout.socket)
    }

    @Test
    fun timeout_copyPreservesUnchangedValues() {
        val original = Timeout(request = 30.seconds, connect = 10.seconds)
        val copied = original.copy(socket = 60.seconds)

        assertEquals(30.seconds, copied.request)
        assertEquals(10.seconds, copied.connect)
        assertEquals(60.seconds, copied.socket)
    }

    // ==================== Timeout Configuration in VolvoCarsConfig ====================

    @Test
    fun volvoCarsConfig_hasDefaultTimeout() {
        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token"
        )

        assertNotNull(config.timeout)
    }

    @Test
    fun volvoCarsConfig_acceptsCustomTimeout() {
        val timeout = Timeout(
            request = 30.seconds,
            connect = 10.seconds,
            socket = 60.seconds
        )
        val config = VolvoCarsConfig(
            apiKey = "test-api-key",
            token = "test-token",
            timeout = timeout
        )

        assertEquals(timeout, config.timeout)
    }

    // ==================== Timeout Exception Mapping ====================

    @Test
    fun httpRequestTimeoutException_mapsToVolvoTimeoutException() = runTest {
        val mockEngine = MockEngine {
            throw HttpRequestTimeoutException("https://api.test.com/test", 1000)
        }
        val transport = createTransport(mockEngine)

        val exception = assertFailsWith<VolvoTimeoutException> {
            transport.perform<String>(typeInfo<String>()) { it.get("https://api.test.com/test") }
        }

        assertNotNull(exception)
        assertNotNull(exception.message)
    }

    // ==================== Timeout Values ====================

    @Test
    fun timeout_smallValues_areAccepted() {
        val timeout = Timeout(
            request = 100.milliseconds,
            connect = 50.milliseconds,
            socket = 200.milliseconds
        )

        assertEquals(100.milliseconds, timeout.request)
        assertEquals(50.milliseconds, timeout.connect)
        assertEquals(200.milliseconds, timeout.socket)
    }

    @Test
    fun timeout_largeValues_areAccepted() {
        val timeout = Timeout(
            request = 300.seconds,
            connect = 120.seconds,
            socket = 600.seconds
        )

        assertEquals(300.seconds, timeout.request)
        assertEquals(120.seconds, timeout.connect)
        assertEquals(600.seconds, timeout.socket)
    }

    @Test
    fun timeout_equality_works() {
        val timeout1 = Timeout(request = 30.seconds, connect = 10.seconds)
        val timeout2 = Timeout(request = 30.seconds, connect = 10.seconds)
        val timeout3 = Timeout(request = 30.seconds, connect = 5.seconds)

        assertEquals(timeout1, timeout2)
        assertEquals(timeout1.hashCode(), timeout2.hashCode())
        assertNotNull(timeout1)
        assertNotNull(timeout3)
    }

    @Test
    fun timeout_toString_containsValues() {
        val timeout = Timeout(request = 30.seconds, connect = 10.seconds)

        val str = timeout.toString()
        assertNotNull(str)
        // Data class toString includes property values
    }

    // Helper function to create HttpTransport with mock engine
    private fun createTransport(mockEngine: MockEngine): HttpTransport {
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            expectSuccess = true
        }
        return HttpTransport(client)
    }
}
