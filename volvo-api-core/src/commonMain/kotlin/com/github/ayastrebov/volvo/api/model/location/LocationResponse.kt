package com.github.ayastrebov.volvo.api.model.location

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle location response.
 */
@Serializable
public data class LocationResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: Feature? = null
)

/**
 * GeoJSON Feature representing the vehicle location.
 */
@Serializable
public data class Feature(
    @SerialName("type") val type: String? = null,
    @SerialName("properties") val properties: Map<String, String>? = null,
    @SerialName("geometry") val geometry: Point? = null
)

/**
 * GeoJSON Point geometry.
 *
 * Coordinates are in [longitude, latitude, altitude] format.
 * Altitude is optional.
 */
@Serializable
public data class Point(
    @SerialName("type") val type: String? = null,
    @SerialName("coordinates") val coordinates: List<Double>? = null
) {
    /** Longitude coordinate. */
    public val longitude: Double?
        get() = coordinates?.getOrNull(0)

    /** Latitude coordinate. */
    public val latitude: Double?
        get() = coordinates?.getOrNull(1)

    /** Altitude coordinate (optional). */
    public val altitude: Double?
        get() = coordinates?.getOrNull(2)
}
