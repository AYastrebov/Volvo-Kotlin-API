package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle diagnostics response.
 */
@Serializable
public data class DiagnosticsResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: Diagnostics? = null
)

/**
 * Vehicle diagnostic values.
 */
@Serializable
public data class Diagnostics(
    @SerialName("serviceWarning") val serviceWarning: ValueWithTimestamp<String>? = null,
    @SerialName("serviceTrigger") val serviceTrigger: ValueWithTimestamp<String>? = null,
    @SerialName("engineHoursToService") val engineHoursToService: ValueWithTimestamp<Int>? = null,
    @SerialName("distanceToService") val distanceToService: ValueWithTimestamp<Int>? = null,
    @SerialName("washerFluidLevelWarning") val washerFluidLevelWarning: ValueWithTimestamp<String>? = null,
    @SerialName("timeToService") val timeToService: ValueWithTimestamp<Int>? = null
)
