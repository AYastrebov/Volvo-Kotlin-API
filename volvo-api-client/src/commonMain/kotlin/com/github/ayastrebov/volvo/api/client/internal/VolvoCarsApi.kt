package com.github.ayastrebov.volvo.api.client.internal

import com.github.ayastrebov.volvo.api.api.ConnectedVehicle
import com.github.ayastrebov.volvo.api.api.Energy
import com.github.ayastrebov.volvo.api.api.ExtendedVehicle
import com.github.ayastrebov.volvo.api.api.Location
import com.github.ayastrebov.volvo.api.client.VolvoCars
import com.github.ayastrebov.volvo.api.client.internal.api.ConnectedVehicleApi
import com.github.ayastrebov.volvo.api.client.internal.api.EnergyApi
import com.github.ayastrebov.volvo.api.client.internal.api.ExtendedVehicleApi
import com.github.ayastrebov.volvo.api.client.internal.api.LocationApi
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester

/**
 * Implementation of [VolvoCars] that delegates to specialized API implementations.
 *
 * This class uses Kotlin's delegation pattern to compose multiple API interfaces
 * into a single unified client. Each API domain ([ConnectedVehicle], [Energy],
 * [ExtendedVehicle], [Location]) is implemented by a dedicated class that handles
 * the specific endpoints for that domain.
 *
 * The delegation approach provides:
 * - Clean separation of concerns between API domains
 * - Reuse of the shared [HttpRequester] for all HTTP operations
 * - Automatic implementation of [AutoCloseable] via the requester
 *
 * @param requester The HTTP transport layer used for all API requests
 */
internal class VolvoCarsApi(
    private val requester: HttpRequester
) : VolvoCars,
    ConnectedVehicle by ConnectedVehicleApi(requester),
    Energy by EnergyApi(requester),
    ExtendedVehicle by ExtendedVehicleApi(requester),
    Location by LocationApi(requester),
    AutoCloseable by requester