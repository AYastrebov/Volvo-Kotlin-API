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
