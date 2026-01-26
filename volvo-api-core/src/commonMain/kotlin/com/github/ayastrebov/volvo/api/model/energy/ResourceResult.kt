package com.github.ayastrebov.volvo.api.model.energy

import com.github.ayastrebov.volvo.api.model.common.ChargerConnectionStatus
import com.github.ayastrebov.volvo.api.model.common.ChargingStatus
import com.github.ayastrebov.volvo.api.model.common.ChargingType
import com.github.ayastrebov.volvo.api.model.common.ResourceStatus
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base result with status indicator.
 */
@Serializable
public data class ResourceResultString(
    @SerialName("status") val status: String,
    @SerialName("value") val value: String? = null,
    @SerialName("updatedAt") val updatedAt: Instant? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("message") val message: String? = null
)

/**
 * Resource result with integer value and unit.
 */
@Serializable
public data class ResourceResultIntegerWithUnit(
    @SerialName("status") val status: String,
    @SerialName("value") val value: Int? = null,
    @SerialName("updatedAt") val updatedAt: Instant? = null,
    @SerialName("unit") val unit: String? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("message") val message: String? = null
)

/**
 * Resource result with float value and unit.
 */
@Serializable
public data class ResourceResultFloatWithUnit(
    @SerialName("status") val status: String,
    @SerialName("value") val value: Float? = null,
    @SerialName("updatedAt") val updatedAt: Instant? = null,
    @SerialName("unit") val unit: String? = null,
    @SerialName("code") val code: String? = null,
    @SerialName("message") val message: String? = null
)

// ==================== Type-Safe Status Extensions ====================

/**
 * Returns the [status] as a type-safe [ResourceStatus] enum.
 *
 * @return The [ResourceStatus] enum, or [ResourceStatus.UNKNOWN] if not recognized.
 */
public val ResourceResultString.resourceStatus: ResourceStatus
    get() = ResourceStatus.fromString(status)

/**
 * Returns the [status] as a type-safe [ResourceStatus] enum.
 *
 * @return The [ResourceStatus] enum, or [ResourceStatus.UNKNOWN] if not recognized.
 */
public val ResourceResultIntegerWithUnit.resourceStatus: ResourceStatus
    get() = ResourceStatus.fromString(status)

/**
 * Returns the [status] as a type-safe [ResourceStatus] enum.
 *
 * @return The [ResourceStatus] enum, or [ResourceStatus.UNKNOWN] if not recognized.
 */
public val ResourceResultFloatWithUnit.resourceStatus: ResourceStatus
    get() = ResourceStatus.fromString(status)

/**
 * Returns the [value] as a type-safe [ChargerConnectionStatus] enum.
 *
 * Use this extension when the [ResourceResultString] represents a charger connection status.
 *
 * @return The [ChargerConnectionStatus] enum, or [ChargerConnectionStatus.UNKNOWN] if not recognized.
 */
public val ResourceResultString.chargerConnectionStatusValue: ChargerConnectionStatus
    get() = ChargerConnectionStatus.fromString(value)

/**
 * Returns the [value] as a type-safe [ChargingStatus] enum.
 *
 * Use this extension when the [ResourceResultString] represents a charging status.
 *
 * @return The [ChargingStatus] enum, or [ChargingStatus.UNKNOWN] if not recognized.
 */
public val ResourceResultString.chargingStatusValue: ChargingStatus
    get() = ChargingStatus.fromString(value)

/**
 * Returns the [value] as a type-safe [ChargingType] enum.
 *
 * Use this extension when the [ResourceResultString] represents a charging type.
 *
 * @return The [ChargingType] enum, or [ChargingType.UNKNOWN] if not recognized.
 */
public val ResourceResultString.chargingTypeValue: ChargingType
    get() = ChargingType.fromString(value)
