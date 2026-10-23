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
 * Fuel and battery levels.
 *
 * The published specification only lists [fuelAmount]; the live API also returns
 * [batteryChargeLevel] (scope `conve:battery_charge_level`) for hybrids and electric vehicles.
 */
@Serializable
public data class FuelAmount(
    /** Remaining fuel, in liters. */
    @SerialName("fuelAmount") val fuelAmount: ValueWithTimestamp<Double>? = null,

    /** High-voltage battery charge level, in percent. */
    @SerialName("batteryChargeLevel") val batteryChargeLevel: ValueWithTimestamp<Double>? = null
)
