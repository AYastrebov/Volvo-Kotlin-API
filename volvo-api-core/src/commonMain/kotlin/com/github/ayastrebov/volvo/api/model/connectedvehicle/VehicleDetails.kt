package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle details response.
 */
@Serializable
public data class VehicleDetailsResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: VehicleDetails? = null
)

/**
 * Detailed information about a vehicle.
 */
@Serializable
public data class VehicleDetails(
    @SerialName("vin") val vin: String? = null,
    @SerialName("modelYear") val modelYear: Int? = null,
    @SerialName("gearbox") val gearbox: String? = null,
    @SerialName("fuelType") val fuelType: String? = null,
    @SerialName("externalColour") val externalColour: String? = null,
    @SerialName("batteryCapacityKWH") val batteryCapacityKWH: Double? = null,
    @SerialName("images") val images: VehicleImages? = null,
    @SerialName("descriptions") val descriptions: VehicleDescriptions? = null
)

/**
 * Vehicle image URLs.
 */
@Serializable
public data class VehicleImages(
    @SerialName("exteriorImageUrl") val exteriorImageUrl: String? = null,
    @SerialName("internalImageUrl") val internalImageUrl: String? = null
)

/**
 * Vehicle descriptions.
 */
@Serializable
public data class VehicleDescriptions(
    @SerialName("model") val model: String? = null,
    @SerialName("upholstery") val upholstery: String? = null,
    @SerialName("steering") val steering: String? = null
)
