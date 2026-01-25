package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle brake status response.
 */
@Serializable
public data class BrakeStatusResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: BrakeStatus? = null
)

/**
 * Brake status values.
 */
@Serializable
public data class BrakeStatus(
    @SerialName("brakeFluidLevelWarning") val brakeFluidLevelWarning: ValueWithTimestamp<String>? = null
)
