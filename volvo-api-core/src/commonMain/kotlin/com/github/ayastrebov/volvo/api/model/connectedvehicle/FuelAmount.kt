package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle fuel amount response.
 */
@Serializable
public data class FuelAmountResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: FuelAmount? = null
)

/**
 * Fuel amount value in liters.
 */
@Serializable
public data class FuelAmount(
    @SerialName("fuelAmount") val fuelAmount: ValueWithTimestamp<Double>? = null
)
