package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for engine start command.
 *
 * @property runtimeMinutes The duration for which the engine should run (0-15 minutes).
 * @throws IllegalArgumentException if [runtimeMinutes] is outside the 0-15 range the API accepts.
 */
@Serializable
public data class EngineStartRequest(
    @SerialName("runtimeMinutes") val runtimeMinutes: Int
) {
    init {
        require(runtimeMinutes in MIN_RUNTIME_MINUTES..MAX_RUNTIME_MINUTES) {
            "runtimeMinutes must be between $MIN_RUNTIME_MINUTES and $MAX_RUNTIME_MINUTES, but was $runtimeMinutes"
        }
    }

    public companion object {
        /** Smallest runtime the API accepts. */
        public const val MIN_RUNTIME_MINUTES: Int = 0

        /** Largest runtime the API accepts. */
        public const val MAX_RUNTIME_MINUTES: Int = 15
    }
}

/**
 * Empty request body for command invocations that require no parameters.
 *
 * Serializes to `{}` and ensures consistent serialization behavior across all command invocations.
 */
@Serializable
public data object EmptyCommandRequest
