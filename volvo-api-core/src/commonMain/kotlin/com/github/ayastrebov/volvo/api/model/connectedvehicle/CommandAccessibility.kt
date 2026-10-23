package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlin.time.Instant
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
 *
 * Tells whether the vehicle can currently receive remote commands.
 */
@Serializable
public data class CommandAccessibility(
    @SerialName("availabilityStatus") val availabilityStatus: AvailabilityStatus? = null
)

/**
 * Whether the vehicle is reachable for remote commands.
 *
 * @property value Availability value, e.g. `AVAILABLE` or `UNAVAILABLE`.
 * @property unavailableReason Reason the vehicle is unavailable, present when [value] is `UNAVAILABLE`
 *                             (e.g. `CAR_IN_USE`, `NO_INTERNET`, `POWER_SAVING_MODE`).
 * @property timestamp Instant when the availability was last evaluated.
 */
@Serializable
public data class AvailabilityStatus(
    @SerialName("value") val value: String? = null,
    @SerialName("unavailableReason") val unavailableReason: String? = null,
    @SerialName("timestamp") val timestamp: Instant? = null
)
