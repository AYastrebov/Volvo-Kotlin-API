package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response for a command invocation.
 */
@Serializable
public data class CommandResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: InvokeResult? = null
)

/**
 * Command invocation result.
 */
@Serializable
public data class InvokeResult(
    @SerialName("vin") val vin: String? = null,
    @SerialName("invokeStatus") val invokeStatus: String? = null,
    @SerialName("message") val message: String? = null
)

/**
 * Response for unlock command invocation with additional unlock-specific fields.
 */
@Serializable
public data class UnlockCommandResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: UnlockInvokeResult? = null
)

/**
 * Unlock command invocation result with additional fields.
 */
@Serializable
public data class UnlockInvokeResult(
    @SerialName("vin") val vin: String? = null,
    @SerialName("invokeStatus") val invokeStatus: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("readyToUnlock") val readyToUnlock: Boolean? = null,
    @SerialName("readyToUnlockUntil") val readyToUnlockUntil: Long? = null
)
