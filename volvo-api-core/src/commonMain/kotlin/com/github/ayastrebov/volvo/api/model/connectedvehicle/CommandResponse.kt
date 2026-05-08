package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.InvokeStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base interface for all command invocation results.
 *
 * All command invocation results share common properties:
 * - [vin]: Vehicle Identification Number
 * - [invokeStatus]: Current status of the command (e.g., "RUNNING", "COMPLETED", "FAILED")
 * - [message]: Optional message providing additional details
 *
 * @see InvokeResult for standard command results
 * @see UnlockInvokeResult for unlock-specific results with additional fields
 */
public interface BaseInvokeResult {
    /** Vehicle Identification Number. */
    public val vin: String?

    /**
     * Current status of the command invocation.
     *
     * Known values:
     * - "RUNNING" - Command is being executed
     * - "COMPLETED" - Command executed successfully
     * - "FAILED" - Command execution failed
     */
    public val invokeStatus: String?

    /** Optional message providing additional details about the command execution. */
    public val message: String?
}

/**
 * Response for a command invocation.
 *
 * This is the standard response returned by most vehicle commands
 * (lock, honk, flash, engine start/stop, climatization).
 *
 * @property status HTTP status code from the operation.
 * @property operationId Unique identifier for tracking this operation.
 * @property data The command invocation result.
 *
 * @see UnlockCommandResponse for unlock-specific response with additional fields.
 */
@Serializable
public data class CommandResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: InvokeResult? = null
)

/**
 * Standard command invocation result.
 *
 * Contains the common result fields for most vehicle commands.
 *
 * @property vin Vehicle Identification Number.
 * @property invokeStatus Current status ("RUNNING", "COMPLETED", "FAILED").
 * @property message Optional details about the command execution.
 */
@Serializable
public data class InvokeResult(
    @SerialName("vin") override val vin: String? = null,
    @SerialName("invokeStatus") override val invokeStatus: String? = null,
    @SerialName("message") override val message: String? = null
) : BaseInvokeResult

/**
 * Response for unlock command invocation with additional unlock-specific fields.
 *
 * The unlock command returns additional information about the unlock state
 * that is not present in standard command responses.
 *
 * @property status HTTP status code from the operation.
 * @property operationId Unique identifier for tracking this operation.
 * @property data The unlock command invocation result with additional fields.
 *
 * @see CommandResponse for standard command responses.
 */
@Serializable
public data class UnlockCommandResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("operationId") val operationId: String? = null,
    @SerialName("data") val data: UnlockInvokeResult? = null
)

/**
 * Unlock command invocation result with additional unlock-specific fields.
 *
 * Extends the standard [BaseInvokeResult] with information about
 * the unlock readiness state.
 *
 * @property vin Vehicle Identification Number.
 * @property invokeStatus Current status ("RUNNING", "COMPLETED", "FAILED").
 * @property message Optional details about the command execution.
 * @property readyToUnlock Whether the vehicle is ready to be unlocked.
 * @property readyToUnlockUntil Timestamp (in milliseconds since epoch) until which
 *                              the vehicle remains ready to unlock.
 */
@Serializable
public data class UnlockInvokeResult(
    @SerialName("vin") override val vin: String? = null,
    @SerialName("invokeStatus") override val invokeStatus: String? = null,
    @SerialName("message") override val message: String? = null,
    @SerialName("readyToUnlock") val readyToUnlock: Boolean? = null,
    @SerialName("readyToUnlockUntil") val readyToUnlockUntil: Long? = null
) : BaseInvokeResult

/**
 * Returns the [BaseInvokeResult.invokeStatus] as a type-safe [InvokeStatus] enum.
 *
 * This provides type-safe access to the invoke status while maintaining
 * backward compatibility with the raw string field.
 *
 * @return The [InvokeStatus] enum, or [InvokeStatus.UNKNOWN] if the status
 *         string is null or not recognized.
 */
public val BaseInvokeResult.status: InvokeStatus
    get() = InvokeStatus.fromString(invokeStatus)
