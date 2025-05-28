package com.github.ayastrebov.volvo.api.exception

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an error response from the Volvo API.
 *
 * @param detail information about the error that occurred.
 */
@Serializable
public data class VolvoApiError(
    @SerialName("error") public val detail: VolvoErrorDetails? = null,
)

/**
 * Represents an error object returned by the Volvo API.
 */
@Serializable
public data class VolvoErrorDetails(
    /**
     * An error code identifying the error type.
     */
    @SerialName("code") val code: String? = null,
    /**
     * A human-readable message providing more details about the error.
     */
    @SerialName("message") val message: String? = null,
    /**
     * The name of the parameter that caused the error, if applicable.
     */
    @SerialName("param") val param: String? = null,
    /**
     * The type of error that occurred.
     */
    @SerialName("type") val type: String? = null,
    /**
     * The line number of the input file where the error occurred, if applicable.
     */
    @SerialName("line") val line: Int? = null,
)
