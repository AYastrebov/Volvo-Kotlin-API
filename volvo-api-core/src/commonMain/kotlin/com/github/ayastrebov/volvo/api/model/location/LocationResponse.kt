package com.github.ayastrebov.volvo.api.model.location

import kotlin.time.Instant
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
 *
 * The API sends every property as a string; [heading] and [timestamp] expose the two it
 * always includes in typed form, while [properties] keeps the raw map for anything else.
 */
@Serializable
public data class Feature(
    @SerialName("type") val type: String? = null,
    @SerialName("properties") val properties: Map<String, String>? = null,
    @SerialName("geometry") val geometry: Point? = null
) {
    /** Vehicle heading in degrees (0-359), or `null` if absent or not numeric. */
    public val heading: Int?
        get() = properties?.get(PROPERTY_HEADING)?.toDoubleOrNull()?.toInt()

    /** Instant the position was recorded, or `null` if absent or not an ISO-8601 instant. */
    public val timestamp: Instant?
        get() = properties?.get(PROPERTY_TIMESTAMP)?.let { runCatching { Instant.parse(it) }.getOrNull() }

    public companion object {
        /** Key of the heading entry in [properties]. */
        public const val PROPERTY_HEADING: String = "heading"

        /** Key of the timestamp entry in [properties]. */
        public const val PROPERTY_TIMESTAMP: String = "timestamp"
    }
}

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
