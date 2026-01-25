package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle warnings response.
 */
@Serializable
public data class WarningsResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: Warnings? = null
)

/**
 * Vehicle exterior warnings (bulb failures, etc.).
 */
@Serializable
public data class Warnings(
    @SerialName("brakeLightCenterWarning") val brakeLightCenterWarning: ValueWithTimestamp<String>? = null,
    @SerialName("brakeLightLeftWarning") val brakeLightLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("brakeLightRightWarning") val brakeLightRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("fogLightFrontWarning") val fogLightFrontWarning: ValueWithTimestamp<String>? = null,
    @SerialName("fogLightRearWarning") val fogLightRearWarning: ValueWithTimestamp<String>? = null,
    @SerialName("positionLightFrontLeftWarning") val positionLightFrontLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("positionLightFrontRightWarning") val positionLightFrontRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("positionLightRearLeftWarning") val positionLightRearLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("positionLightRearRightWarning") val positionLightRearRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("highBeamLeftWarning") val highBeamLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("highBeamRightWarning") val highBeamRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("lowBeamLeftWarning") val lowBeamLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("lowBeamRightWarning") val lowBeamRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("daytimeRunningLightLeftWarning") val daytimeRunningLightLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("daytimeRunningLightRightWarning") val daytimeRunningLightRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("turnIndicationFrontLeftWarning") val turnIndicationFrontLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("turnIndicationFrontRightWarning") val turnIndicationFrontRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("turnIndicationRearLeftWarning") val turnIndicationRearLeftWarning: ValueWithTimestamp<String>? = null,
    @SerialName("turnIndicationRearRightWarning") val turnIndicationRearRightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("registrationPlateLightWarning") val registrationPlateLightWarning: ValueWithTimestamp<String>? = null,
    @SerialName("sideMarkLightsWarning") val sideMarkLightsWarning: ValueWithTimestamp<String>? = null,
    @SerialName("hazardLightsWarning") val hazardLightsWarning: ValueWithTimestamp<String>? = null,
    @SerialName("reverseLightsWarning") val reverseLightsWarning: ValueWithTimestamp<String>? = null
)
