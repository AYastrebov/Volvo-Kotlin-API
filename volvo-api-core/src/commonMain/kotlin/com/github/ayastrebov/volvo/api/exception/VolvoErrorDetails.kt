package com.github.ayastrebov.volvo.api.exception

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an error response from the Volvo API.
 *
 * The Connected Vehicle and Location APIs wrap the error in the same envelope as successful
 * responses; the Energy API returns only the `error` object. Both shapes decode into this class.
 *
 * @param status HTTP status code echoed in the body, when the API includes it.
 * @param operationId Volvo-side identifier of the failed request, useful when contacting Volvo support.
 * @param detail information about the error that occurred.
 */
@Serializable
public data class VolvoApiError(
    @SerialName("status") public val status: Int? = null,
    @SerialName("operationId") public val operationId: String? = null,
    @SerialName("error") public val detail: VolvoErrorDetails? = null,
)

/**
 * Represents an error object returned by the Volvo API.
 *
 * The Connected Vehicle and Location APIs return `message` and `description`; the Energy API
 * returns `code`, `message` and, for client errors, a list of [details].
 */
@Serializable
public data class VolvoErrorDetails(
    /**
     * An error code identifying the error type (Energy API, and the OAuth error code on token refresh failures).
     */
    @SerialName("code") val code: String? = null,
    /**
     * A human-readable message providing more details about the error.
     */
    @SerialName("message") val message: String? = null,
    /**
     * A longer description of the error (Connected Vehicle and Location APIs).
     */
    @SerialName("description") val description: String? = null,
    /**
     * Nested errors giving more detail, e.g. one entry per invalid field (Energy API).
     */
    @SerialName("details") val details: List<VolvoErrorDetails>? = null,
)
