package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.test.ConnectedVehicleFixtures
import com.github.ayastrebov.volvo.api.client.test.TestData
import com.github.ayastrebov.volvo.api.core.OAuthConfig
import com.github.ayastrebov.volvo.api.exception.AuthenticationException
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for automatic OAuth2 token refresh against the Volvo ID token endpoint.
 */
class TokenRefreshTest {

    private val tokenUrl = "https://volvoid.test/as/token.oauth2"

    private fun oauthConfig(onTokensRefreshed: ((String, String) -> Unit)? = null) = OAuthConfig(
        accessToken = "expired-token",
        refreshToken = "refresh-1",
        clientId = "client-id",
        clientSecret = "client-secret",
        tokenUrl = tokenUrl,
        onTokensRefreshed = onTokensRefreshed,
    )

    private fun MockRequestHandleScope.json(body: String, status: HttpStatusCode = HttpStatusCode.OK) =
        respond(body, status, headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))

    private fun MockRequestHandleScope.apiResponse(request: HttpRequestData) =
        if (request.headers[HttpHeaders.Authorization] == "Bearer fresh-token") {
            json(ConnectedVehicleFixtures.vehicleListResponse)
        } else {
            json("""{"error":{"message":"Unauthorized","description":"Token expired"}}""", HttpStatusCode.Unauthorized)
        }

    @Test
    fun expiredToken_isRefreshedAndRequestRetried() = runTest {
        val tokenRequests = mutableListOf<HttpRequestData>()
        val engine = MockEngine { request ->
            if (request.url.toString() == tokenUrl) {
                tokenRequests += request
                json("""{"access_token":"fresh-token","refresh_token":"refresh-2","token_type":"Bearer","expires_in":1800}""")
            } else {
                apiResponse(request)
            }
        }
        var refreshed: Pair<String, String>? = null
        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                oauth = oauthConfig { access, refresh -> refreshed = access to refresh },
                engine = engine,
            )
        )

        val response = client.getVehicleList()

        assertEquals(200, response.status)
        assertEquals("fresh-token" to "refresh-2", refreshed)
        assertEquals(1, tokenRequests.size)
        val tokenRequest = tokenRequests.single()
        assertEquals(HttpMethod.Post, tokenRequest.method)
        val form = (tokenRequest.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
        assertTrue(form.contains("grant_type=refresh_token"), form)
        assertTrue(form.contains("refresh_token=refresh-1"), form)
        // client_secret_basic, as documented on the Volvo developer portal
        val authorization = tokenRequest.headers[HttpHeaders.Authorization]
        assertNotNull(authorization, "No Authorization header; headers were ${tokenRequest.headers.entries()}")
        assertTrue(authorization.startsWith("Basic "), authorization)
    }

    @Test
    fun refreshWithoutRotatedRefreshToken_keepsPreviousRefreshToken() = runTest {
        val engine = MockEngine { request ->
            if (request.url.toString() == tokenUrl) {
                json("""{"access_token":"fresh-token","token_type":"Bearer","expires_in":1800}""")
            } else {
                apiResponse(request)
            }
        }
        var refreshed: Pair<String, String>? = null
        val client = VolvoCars(
            VolvoCarsConfig(
                apiKey = TestData.TEST_API_KEY,
                oauth = oauthConfig { access, refresh -> refreshed = access to refresh },
                engine = engine,
            )
        )

        client.getVehicleList()

        assertEquals("fresh-token" to "refresh-1", refreshed)
    }

    @Test
    fun staleRefreshToken_surfacesAsAuthenticationException() = runTest {
        val engine = MockEngine { request ->
            if (request.url.toString() == tokenUrl) {
                json(
                    """{"error":"invalid_grant","error_description":"Refresh token is invalid or expired"}""",
                    HttpStatusCode.BadRequest
                )
            } else {
                apiResponse(request)
            }
        }
        val client = VolvoCars(
            VolvoCarsConfig(apiKey = TestData.TEST_API_KEY, oauth = oauthConfig(), engine = engine)
        )

        val exception = assertFailsWith<AuthenticationException> { client.getVehicleList() }

        assertEquals(400, exception.statusCode)
        assertEquals("invalid_grant", exception.error.detail?.code)
        assertTrue(exception.message!!.contains("Refresh token is invalid or expired"), exception.message)
    }
}
