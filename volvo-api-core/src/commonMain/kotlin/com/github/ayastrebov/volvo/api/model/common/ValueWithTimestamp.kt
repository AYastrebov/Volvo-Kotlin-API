package com.github.ayastrebov.volvo.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A generic value wrapper with timestamp and optional unit.
 *
 * @param T The type of the value
 * @property value The actual value
 * @property timestamp ISO 8601 timestamp when the value was recorded
 * @property unit Optional unit of measurement (e.g., "km", "liters")
 */
@Serializable
public data class ValueWithTimestamp<T>(
    @SerialName("value") val value: T,
    @SerialName("timestamp") val timestamp: String? = null,
    @SerialName("unit") val unit: String? = null
)
