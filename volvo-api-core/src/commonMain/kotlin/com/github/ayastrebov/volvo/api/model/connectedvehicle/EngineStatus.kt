package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle engine status response.
 */
@Serializable
public data class EngineStatusResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: EngineStatus? = null
)

/**
 * Engine status values.
 */
@Serializable
public data class EngineStatus(
    @SerialName("engineStatus") val engineStatus: ValueWithTimestamp<String>? = null
)
