package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle statistics response.
 */
@Serializable
public data class StatisticsResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: Statistics? = null
)

/**
 * Vehicle statistics data.
 */
@Serializable
public data class Statistics(
    @SerialName("averageSpeed") val averageSpeed: ValueWithTimestamp<String>? = null,
    @SerialName("distanceToEmpty") val distanceToEmpty: ValueWithTimestamp<String>? = null,
    @SerialName("tripMeter1") val tripMeter1: ValueWithTimestamp<String>? = null,
    @SerialName("tripMeter2") val tripMeter2: ValueWithTimestamp<String>? = null,
    @SerialName("averageFuelConsumption") val averageFuelConsumption: ValueWithTimestamp<String>? = null
)
