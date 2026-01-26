package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.test.*
import com.github.ayastrebov.volvo.api.exception.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for HTTP exception handling and error mapping.
 */
class ExceptionHandlingTest {

    // ==================== Client Error Handling ====================

    @Test
    fun httpStatus400_throwsInvalidRequestException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error400,
            HttpStatusCode.BadRequest
        )

        val exception = assertFailsWith<InvalidRequestException> {
            client.getVehicleList()
        }

        assertEquals(400, exception.statusCode)
        assertNotNull(exception.error)
        assertEquals("INVALID_REQUEST", exception.error.detail?.code)
        assertEquals("The request body is invalid or malformed", exception.error.detail?.message)
    }

    @Test
    fun httpStatus401_throwsAuthenticationException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error401,
            HttpStatusCode.Unauthorized
        )

        val exception = assertFailsWith<AuthenticationException> {
            client.getVehicleList()
        }

        assertEquals(401, exception.statusCode)
        assertNotNull(exception.error)
        assertEquals("UNAUTHORIZED", exception.error.detail?.code)
        assertEquals("Invalid or expired access token", exception.error.detail?.message)
    }

    @Test
    fun httpStatus403_throwsPermissionException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error403,
            HttpStatusCode.Forbidden
        )

        val exception = assertFailsWith<PermissionException> {
            client.getVehicleList()
        }

        assertEquals(403, exception.statusCode)
        assertNotNull(exception.error)
        assertEquals("FORBIDDEN", exception.error.detail?.code)
        assertEquals("You don't have permission to access this resource", exception.error.detail?.message)
    }

    @Test
    fun httpStatus404_throwsInvalidRequestException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error404,
            HttpStatusCode.NotFound
        )

        val exception = assertFailsWith<InvalidRequestException> {
            client.getVehicleDetails(TestData.TEST_VIN)
        }

        assertEquals(404, exception.statusCode)
        assertNotNull(exception.error)
        assertEquals("NOT_FOUND", exception.error.detail?.code)
        assertEquals("Vehicle not found", exception.error.detail?.message)
    }

    @Test
    fun httpStatus429_throwsRateLimitException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error429,
            HttpStatusCode.TooManyRequests
        )

        val exception = assertFailsWith<RateLimitException> {
            client.getVehicleList()
        }

        assertEquals(429, exception.statusCode)
        assertNotNull(exception.error)
        assertEquals("RATE_LIMIT_EXCEEDED", exception.error.detail?.code)
        assertEquals("Too many requests. Please try again later", exception.error.detail?.message)
    }

    @Test
    fun httpStatus405_throwsInvalidRequestException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.errorGeneric,
            HttpStatusCode.MethodNotAllowed
        )

        val exception = assertFailsWith<InvalidRequestException> {
            client.getVehicleList()
        }

        assertEquals(405, exception.statusCode)
    }

    @Test
    fun httpStatus409_throwsInvalidRequestException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.errorGeneric,
            HttpStatusCode.Conflict
        )

        val exception = assertFailsWith<InvalidRequestException> {
            client.getVehicleList()
        }

        assertEquals(409, exception.statusCode)
    }

    @Test
    fun httpStatus415_throwsInvalidRequestException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.errorGeneric,
            HttpStatusCode.UnsupportedMediaType
        )

        val exception = assertFailsWith<InvalidRequestException> {
            client.getVehicleList()
        }

        assertEquals(415, exception.statusCode)
    }

    @Test
    fun httpStatus410_throwsInvalidRequestException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.errorGeneric,
            HttpStatusCode.Gone
        )

        val exception = assertFailsWith<InvalidRequestException> {
            client.getVehicleList()
        }

        assertEquals(410, exception.statusCode)
    }

    // ==================== Server Error Handling ====================

    @Test
    fun httpStatus500_throwsVolvoServerException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error500,
            HttpStatusCode.InternalServerError
        )

        val exception = assertFailsWith<VolvoServerException> {
            client.getVehicleList()
        }

        assertNotNull(exception.message)
    }

    @Test
    fun httpStatus502_throwsVolvoServerException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error500,
            HttpStatusCode.BadGateway
        )

        val exception = assertFailsWith<VolvoServerException> {
            client.getVehicleList()
        }

        assertNotNull(exception)
    }

    @Test
    fun httpStatus503_throwsVolvoServerException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error500,
            HttpStatusCode.ServiceUnavailable
        )

        val exception = assertFailsWith<VolvoServerException> {
            client.getVehicleList()
        }

        assertNotNull(exception)
    }

    // ==================== Error Message Propagation ====================

    @Test
    fun exceptionMessage_containsErrorDetails() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error400,
            HttpStatusCode.BadRequest
        )

        val exception = assertFailsWith<InvalidRequestException> {
            client.getVehicleList()
        }

        // The exception message should be set from the error details
        assertTrue(exception.message?.contains("invalid") == true || exception.error.detail?.message?.contains("invalid") == true)
    }

    // ==================== Different Endpoints Error Handling ====================

    @Test
    fun getVehicleDetails_with401_throwsAuthenticationException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error401,
            HttpStatusCode.Unauthorized
        )

        assertFailsWith<AuthenticationException> {
            client.getVehicleDetails(TestData.TEST_VIN)
        }
    }

    @Test
    fun getWindowStatus_with403_throwsPermissionException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error403,
            HttpStatusCode.Forbidden
        )

        assertFailsWith<PermissionException> {
            client.getWindowStatus(TestData.TEST_VIN)
        }
    }

    @Test
    fun invokeLock_with429_throwsRateLimitException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error429,
            HttpStatusCode.TooManyRequests
        )

        assertFailsWith<RateLimitException> {
            client.invokeLock(TestData.TEST_VIN)
        }
    }

    @Test
    fun getEnergyState_with404_throwsInvalidRequestException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error404,
            HttpStatusCode.NotFound
        )

        assertFailsWith<InvalidRequestException> {
            client.getEnergyState(TestData.TEST_VIN)
        }
    }

    @Test
    fun getVehicleLocation_with500_throwsVolvoServerException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error500,
            HttpStatusCode.InternalServerError
        )

        assertFailsWith<VolvoServerException> {
            client.getVehicleLocation(TestData.TEST_VIN)
        }
    }

    // ==================== Exception Hierarchy ====================

    @Test
    fun authenticationException_isVolvoAPIException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error401,
            HttpStatusCode.Unauthorized
        )

        val exception = assertFailsWith<VolvoAPIException> {
            client.getVehicleList()
        }

        assertTrue(exception is AuthenticationException)
    }

    @Test
    fun rateLimitException_isVolvoAPIException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error429,
            HttpStatusCode.TooManyRequests
        )

        val exception = assertFailsWith<VolvoAPIException> {
            client.getVehicleList()
        }

        assertTrue(exception is RateLimitException)
    }

    @Test
    fun invalidRequestException_isVolvoAPIException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error400,
            HttpStatusCode.BadRequest
        )

        val exception = assertFailsWith<VolvoAPIException> {
            client.getVehicleList()
        }

        assertTrue(exception is InvalidRequestException)
    }

    @Test
    fun permissionException_isVolvoAPIException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error403,
            HttpStatusCode.Forbidden
        )

        val exception = assertFailsWith<VolvoAPIException> {
            client.getVehicleList()
        }

        assertTrue(exception is PermissionException)
    }

    @Test
    fun allApiExceptions_areVolvoException() = runTest {
        val client = createTestClientWithResponse(
            ErrorFixtures.error400,
            HttpStatusCode.BadRequest
        )

        val exception = assertFailsWith<VolvoException> {
            client.getVehicleList()
        }

        assertTrue(exception is VolvoAPIException)
    }
}
