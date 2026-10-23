package com.github.ayastrebov.volvo.api.model.common

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A generic value wrapper with timestamp and optional unit.
 *
 * The Volvo API does not guarantee that `value` is present, so it is nullable:
 * a missing field must not fail deserialization of the whole response.
 *
 * @param T The type of the value
 * @property value The actual value, or `null` if the API omitted it
 * @property timestamp Instant when the value was recorded
 * @property unit Optional unit of measurement (e.g., "km", "liters")
 */
@Serializable
public data class ValueWithTimestamp<T>(
    @SerialName("value") val value: T? = null,
    @SerialName("timestamp") val timestamp: Instant? = null,
    @SerialName("unit") val unit: String? = null
)
