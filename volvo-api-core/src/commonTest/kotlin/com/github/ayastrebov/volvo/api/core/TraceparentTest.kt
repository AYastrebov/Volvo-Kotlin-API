package com.github.ayastrebov.volvo.api.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/**
 * Tests for W3C Trace Context traceparent header generation.
 */
class TraceparentTest {

    private val traceparentRegex = Regex("^00-[0-9a-f]{32}-[0-9a-f]{16}-01$")

    @Test
    fun generate_producesValidFormat() {
        val traceparent = Traceparent.generate()
        assertTrue(traceparentRegex.matches(traceparent.value),
            "traceparent value must match '00-{32hex}-{16hex}-01', but was: ${traceparent.value}")
    }

    @Test
    fun generate_traceIdIs32HexChars() {
        val traceparent = Traceparent.generate()
        assertEquals(32, traceparent.traceId.length,
            "traceId must be 32 characters long")
        assertTrue(traceparent.traceId.all { it in '0'..'9' || it in 'a'..'f' },
            "traceId must contain only lowercase hex characters, but was: ${traceparent.traceId}")
    }

    @Test
    fun generate_parentIdIs16HexChars() {
        val traceparent = Traceparent.generate()
        assertEquals(16, traceparent.parentId.length,
            "parentId must be 16 characters long")
        assertTrue(traceparent.parentId.all { it in '0'..'9' || it in 'a'..'f' },
            "parentId must contain only lowercase hex characters, but was: ${traceparent.parentId}")
    }

    @Test
    fun generate_valueContainsTraceIdAndParentId() {
        val traceparent = Traceparent.generate()
        val expected = "00-${traceparent.traceId}-${traceparent.parentId}-01"
        assertEquals(expected, traceparent.value,
            "value must be composed of version, traceId, parentId, and trace flags")
    }

    @Test
    fun generate_twoCalls_produceDifferentValues() {
        val first = Traceparent.generate()
        val second = Traceparent.generate()
        assertNotEquals(first.value, second.value,
            "Two generate() calls must produce different values")
        assertNotEquals(first.traceId, second.traceId,
            "Two generate() calls must produce different traceIds")
    }

    @Test
    fun generate_toStringReturnsValue() {
        val traceparent = Traceparent.generate()
        assertEquals(traceparent.value, traceparent.toString(),
            "toString() must return the same string as value")
    }
}
