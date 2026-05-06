package com.github.ayastrebov.volvo.api.exception

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for the Volvo exception hierarchy.
 */
class ExceptionHierarchyTest {

    @Test
    fun volvoHttpException_extendsVolvoException() {
        val exception: Any = VolvoHttpException(RuntimeException("Test error"))
        assertIs<VolvoException>(exception)
    }

    @Test
    fun volvoHttpException_preservesCause() {
        val cause = RuntimeException("Original error")
        val exception = VolvoHttpException(cause)

        assertEquals(cause, exception.cause)
        assertEquals("Original error", exception.message)
    }

    @Test
    fun volvoServerException_extendsVolvoException() {
        val exception: Any = VolvoServerException(RuntimeException("Server error"))
        assertIs<VolvoException>(exception)
    }

    @Test
    fun volvoServerException_preservesCause() {
        val cause = RuntimeException("Server error")
        val exception = VolvoServerException(cause)

        assertEquals(cause, exception.cause)
        assertEquals("Server error", exception.message)
    }

    @Test
    fun volvoHttpException_withNullCause() {
        val exception = VolvoHttpException(null)

        assertEquals(null, exception.cause)
        assertEquals(null, exception.message)
    }

    @Test
    fun volvoServerException_withNullCause() {
        val exception = VolvoServerException(null)

        assertEquals(null, exception.cause)
        assertEquals(null, exception.message)
    }

    @Test
    fun volvoException_isSealedClass() {
        // This test verifies that VolvoException subclasses are properly typed
        val exceptions: List<VolvoException> = listOf(
            VolvoHttpException(RuntimeException("Http")),
            VolvoServerException(RuntimeException("Server"))
        )

        assertEquals(2, exceptions.size)

        val httpCount = exceptions.count { it is VolvoHttpException }
        val serverCount = exceptions.count { it is VolvoServerException }

        assertEquals(1, httpCount)
        assertEquals(1, serverCount)
    }
}
