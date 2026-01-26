package com.github.ayastrebov.volvo.api.model.energy

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base result with status indicator.
 */
@Serializable
public data class ResourceResultString(
    @SerialName("status") val status: String,
    @SerialName("value") val value: String? = null,
    @SerialName("updatedAt") val updatedAt: Instant? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("message") val message: String? = null
)

/**
 * Resource result with integer value and unit.
 */
@Serializable
public data class ResourceResultIntegerWithUnit(
    @SerialName("status") val status: String,
    @SerialName("value") val value: Int? = null,
    @SerialName("updatedAt") val updatedAt: Instant? = null,
    @SerialName("unit") val unit: String? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("message") val message: String? = null
)

/**
 * Resource result with float value and unit.
 */
@Serializable
public data class ResourceResultFloatWithUnit(
    @SerialName("status") val status: String,
    @SerialName("value") val value: Float? = null,
    @SerialName("updatedAt") val updatedAt: Instant? = null,
    @SerialName("unit") val unit: String? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("message") val message: String? = null
)
