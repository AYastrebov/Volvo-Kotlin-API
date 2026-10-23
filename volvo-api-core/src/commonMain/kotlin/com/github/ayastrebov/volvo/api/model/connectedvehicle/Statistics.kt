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
 * Vehicle trip statistics.
 *
 * The live API returns the "automatic" / "manual" trip meter pairs and separate
 * distance-to-empty values for tank and battery; the published OpenAPI specification still
 * lists the older [distanceToEmpty], [tripMeter1] and [tripMeter2] names, so both sets are
 * modelled. Values are numeric on the wire (the specification says string); lenient parsing
 * accepts either.
 */
@Serializable
public data class Statistics(
    /** Average speed since the automatic trip meter was reset, in km/h. */
    @SerialName("averageSpeed") val averageSpeed: ValueWithTimestamp<Double>? = null,

    /** Average speed since the automatic trip meter was last reset, in km/h. */
    @SerialName("averageSpeedAutomatic") val averageSpeedAutomatic: ValueWithTimestamp<Double>? = null,

    /** Average fuel consumption, typically in l/100km. */
    @SerialName("averageFuelConsumption") val averageFuelConsumption: ValueWithTimestamp<Double>? = null,

    /** Average fuel consumption since the automatic trip meter was reset, typically in l/100km. */
    @SerialName("averageFuelConsumptionAutomatic") val averageFuelConsumptionAutomatic: ValueWithTimestamp<Double>? = null,

    /** Average electric energy consumption, typically in kWh/100km. */
    @SerialName("averageEnergyConsumption") val averageEnergyConsumption: ValueWithTimestamp<Double>? = null,

    /** Average electric energy consumption since the automatic trip meter was reset, typically in kWh/100km. */
    @SerialName("averageEnergyConsumptionAutomatic") val averageEnergyConsumptionAutomatic: ValueWithTimestamp<Double>? = null,

    /** Average electric energy consumption since the last charge, typically in kWh/100km. */
    @SerialName("averageEnergyConsumptionSinceCharge") val averageEnergyConsumptionSinceCharge: ValueWithTimestamp<Double>? = null,

    /** Estimated range on the remaining fuel, in km. */
    @SerialName("distanceToEmptyTank") val distanceToEmptyTank: ValueWithTimestamp<Double>? = null,

    /** Estimated range on the remaining battery charge, in km. */
    @SerialName("distanceToEmptyBattery") val distanceToEmptyBattery: ValueWithTimestamp<Double>? = null,

    /** Automatic trip meter reading, in km. */
    @SerialName("tripMeterAutomatic") val tripMeterAutomatic: ValueWithTimestamp<Double>? = null,

    /** Manual trip meter reading, in km. */
    @SerialName("tripMeterManual") val tripMeterManual: ValueWithTimestamp<Double>? = null,

    /** Estimated distance to empty, in km. Listed in the specification; not returned by current vehicles. */
    @SerialName("distanceToEmpty") val distanceToEmpty: ValueWithTimestamp<Double>? = null,

    /** Trip meter 1 reading, in km. Listed in the specification; not returned by current vehicles. */
    @SerialName("tripMeter1") val tripMeter1: ValueWithTimestamp<Double>? = null,

    /** Trip meter 2 reading, in km. Listed in the specification; not returned by current vehicles. */
    @SerialName("tripMeter2") val tripMeter2: ValueWithTimestamp<Double>? = null
)
