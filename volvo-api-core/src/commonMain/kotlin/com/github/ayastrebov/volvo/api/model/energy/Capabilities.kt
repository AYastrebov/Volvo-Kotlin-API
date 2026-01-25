package com.github.ayastrebov.volvo.api.model.energy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle capabilities response.
 */
@Serializable
public data class CapabilitiesResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: Capabilities? = null
)

/**
 * Summary of supported endpoints and data points for a vehicle.
 */
@Serializable
public data class Capabilities(
    @SerialName("getEnergyState") val getEnergyState: GetEnergyStateCapability? = null
)

/**
 * Capability information for the energy state endpoint.
 */
@Serializable
public data class GetEnergyStateCapability(
    @SerialName("isSupported") val isSupported: Boolean? = null,
    @SerialName("batteryChargeLevel") val batteryChargeLevel: Capability? = null,
    @SerialName("electricRange") val electricRange: Capability? = null,
    @SerialName("chargerConnectionStatus") val chargerConnectionStatus: Capability? = null,
    @SerialName("chargingSystemStatus") val chargingSystemStatus: Capability? = null,
    @SerialName("chargingType") val chargingType: Capability? = null,
    @SerialName("chargerPowerStatus") val chargerPowerStatus: Capability? = null,
    @SerialName("estimatedChargingTimeToTargetBatteryChargeLevel") val estimatedChargingTimeToTargetBatteryChargeLevel: Capability? = null,
    @SerialName("chargingCurrentLimit") val chargingCurrentLimit: Capability? = null,
    @SerialName("targetBatteryChargeLevel") val targetBatteryChargeLevel: Capability? = null,
    @SerialName("chargingPower") val chargingPower: Capability? = null
)

/**
 * Individual capability indicator.
 */
@Serializable
public data class Capability(
    @SerialName("isSupported") val isSupported: Boolean? = null
)
