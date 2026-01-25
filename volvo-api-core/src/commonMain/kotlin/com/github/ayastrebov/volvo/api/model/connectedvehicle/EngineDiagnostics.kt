package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle engine diagnostics response.
 */
@Serializable
public data class EngineDiagnosticsResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: EngineDiagnostics? = null
)

/**
 * Engine diagnostic values.
 */
@Serializable
public data class EngineDiagnostics(
    @SerialName("oilLevelWarning") val oilLevelWarning: ValueWithTimestamp<String>? = null,
    @SerialName("engineCoolantLevelWarning") val engineCoolantLevelWarning: ValueWithTimestamp<String>? = null
)
