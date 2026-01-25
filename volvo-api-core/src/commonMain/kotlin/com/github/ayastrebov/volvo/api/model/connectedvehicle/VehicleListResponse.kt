package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response containing a list of vehicles associated with the user.
 */
@Serializable
public data class VehicleListResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: List<VehicleVin>? = null
)

/**
 * Vehicle identification.
 */
@Serializable
public data class VehicleVin(
    @SerialName("vin") val vin: String
)
