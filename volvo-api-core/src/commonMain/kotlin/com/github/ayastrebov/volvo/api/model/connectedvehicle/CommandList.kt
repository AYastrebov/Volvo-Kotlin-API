package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle command list response.
 */
@Serializable
public data class CommandListResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: List<Command>? = null
)

/**
 * Available command for the vehicle.
 */
@Serializable
public data class Command(
    @SerialName("command") val command: String? = null,
    @SerialName("href") val href: String? = null
)
