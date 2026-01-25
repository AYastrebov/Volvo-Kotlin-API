package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle tyre pressure status response.
 */
@Serializable
public data class TyreStatusResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: TyreStatus? = null
)

/**
 * Tyre pressure values for all wheels.
 */
@Serializable
public data class TyreStatus(
    @SerialName("frontLeft") val frontLeft: TyrePressureValue? = null,
    @SerialName("frontRight") val frontRight: TyrePressureValue? = null,
    @SerialName("rearLeft") val rearLeft: TyrePressureValue? = null,
    @SerialName("rearRight") val rearRight: TyrePressureValue? = null
)

/**
 * Individual tyre pressure value.
 */
@Serializable
public data class TyrePressureValue(
    @SerialName("value") val value: String? = null,
    @SerialName("timestamp") val timestamp: String? = null
)
