package com.github.ayastrebov.volvo.api.model.energy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Latest energy state data for the vehicle.
 *
 * Unlike the Connected Vehicle and Location APIs, the Energy API v2 returns this object
 * directly as the response body, without a `status`/`operationId`/`data` envelope.
 */
@Serializable
public data class EnergyState(
    /** Battery charge level as percentage. */
    @SerialName("batteryChargeLevel") val batteryChargeLevel: ResourceResultFloatWithUnit? = null,

    /** Electric range in kilometers. */
    @SerialName("electricRange") val electricRange: ResourceResultIntegerWithUnit? = null,

    /** Charger connection status (e.g., CONNECTED, DISCONNECTED). */
    @SerialName("chargerConnectionStatus") val chargerConnectionStatus: ResourceResultString? = null,

    /** Charging status (e.g., IDLE, CHARGING). */
    @SerialName("chargingStatus") val chargingStatus: ResourceResultString? = null,

    /** Type of charging (e.g., NONE, AC, DC). */
    @SerialName("chargingType") val chargingType: ResourceResultString? = null,

    /** Charger power status. */
    @SerialName("chargerPowerStatus") val chargerPowerStatus: ResourceResultString? = null,

    /** Estimated time to reach target battery charge level in minutes. */
    @SerialName("estimatedChargingTimeToTargetBatteryChargeLevel") val estimatedChargingTimeToTargetBatteryChargeLevel: ResourceResultIntegerWithUnit? = null,

    /** Target battery charge level as percentage. */
    @SerialName("targetBatteryChargeLevel") val targetBatteryChargeLevel: ResourceResultIntegerWithUnit? = null,

    /** Charging current limit in amperes. */
    @SerialName("chargingCurrentLimit") val chargingCurrentLimit: ResourceResultIntegerWithUnit? = null,

    /** Charging power in watts/kilowatts. */
    @SerialName("chargingPower") val chargingPower: ResourceResultIntegerWithUnit? = null
)
