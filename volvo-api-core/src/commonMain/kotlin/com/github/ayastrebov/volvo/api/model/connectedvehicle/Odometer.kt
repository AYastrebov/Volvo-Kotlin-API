package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle odometer response.
 */
@Serializable
public data class OdometerResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: Odometer? = null
)

/**
 * Odometer value.
 */
@Serializable
public data class Odometer(
    @SerialName("odometer") val odometer: ValueWithTimestamp<Int>? = null
)
