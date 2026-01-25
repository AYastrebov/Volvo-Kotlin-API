package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle window status response.
 */
@Serializable
public data class WindowStatusResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: WindowStatus? = null
)

/**
 * Status of vehicle windows.
 */
@Serializable
public data class WindowStatus(
    @SerialName("frontLeftWindow") val frontLeftWindow: ValueWithTimestamp<String>? = null,
    @SerialName("frontRightWindow") val frontRightWindow: ValueWithTimestamp<String>? = null,
    @SerialName("rearLeftWindow") val rearLeftWindow: ValueWithTimestamp<String>? = null,
    @SerialName("rearRightWindow") val rearRightWindow: ValueWithTimestamp<String>? = null,
    @SerialName("sunroof") val sunroof: ValueWithTimestamp<String>? = null
)
