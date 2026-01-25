package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle door and lock status response.
 */
@Serializable
public data class DoorAndLockStatusResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: DoorAndLockStatus? = null
)

/**
 * Door and lock status values.
 */
@Serializable
public data class DoorAndLockStatus(
    @SerialName("centralLock") val centralLock: ValueWithTimestamp<String>? = null,
    @SerialName("frontLeftDoor") val frontLeftDoor: ValueWithTimestamp<String>? = null,
    @SerialName("frontRightDoor") val frontRightDoor: ValueWithTimestamp<String>? = null,
    @SerialName("rearLeftDoor") val rearLeftDoor: ValueWithTimestamp<String>? = null,
    @SerialName("rearRightDoor") val rearRightDoor: ValueWithTimestamp<String>? = null,
    @SerialName("hood") val hood: ValueWithTimestamp<String>? = null,
    @SerialName("tailgate") val tailgate: ValueWithTimestamp<String>? = null,
    @SerialName("tankLid") val tankLid: ValueWithTimestamp<String>? = null
)
