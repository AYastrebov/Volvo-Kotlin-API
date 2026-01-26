package com.github.ayastrebov.volvo.api.model.connectedvehicle

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for engine start command.
 *
 * @property runtimeMinutes The duration for which the engine should run (0-15 minutes).
 */
@Serializable
public data class EngineStartRequest(
    @SerialName("runtimeMinutes") val runtimeMinutes: Int
)

/**
 * Empty request body for command invocations that require no parameters.
 *
 * Serializes to `{}` and ensures consistent serialization behavior across all command invocations.
 */
@Serializable
public data object EmptyCommandRequest
