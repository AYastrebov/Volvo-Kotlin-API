package com.github.ayastrebov.volvo.api.client.internal

import com.github.ayastrebov.volvo.api.InternalVolvoApi
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
 * Implementation of [VolvoCars].
 *
 * @param requester http transport layer
 */
@InternalVolvoApi
internal class VolvoCarsApi(
    private val requester: HttpRequester
) : VolvoCars,
    ConnectedVehicle by ConnectedVehicleApi(requester),
    Energy by EnergyApi(requester),
    ExtendedVehicle by ExtendedVehicleApi(requester),
    Location by LocationApi(requester),
    AutoCloseable by requester