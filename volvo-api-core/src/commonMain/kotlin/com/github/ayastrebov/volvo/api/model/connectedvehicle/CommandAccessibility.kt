package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Vehicle command accessibility response.
 */
@Serializable
public data class CommandAccessibilityResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: CommandAccessibility? = null
)

/**
 * Command accessibility status.
 */
@Serializable
public data class CommandAccessibility(
    @SerialName("availableCommands") val availableCommands: List<AvailableCommand>? = null,
    @SerialName("unavailableCommands") val unavailableCommands: List<UnavailableCommand>? = null
)

/**
 * Available command with its accessibility status.
 */
@Serializable
public data class AvailableCommand(
    @SerialName("command") val command: String? = null,
    @SerialName("href") val href: String? = null
)

/**
 * Unavailable command with reason.
 */
@Serializable
public data class UnavailableCommand(
    @SerialName("command") val command: String? = null,
    @SerialName("reason") val reason: String? = null
)
