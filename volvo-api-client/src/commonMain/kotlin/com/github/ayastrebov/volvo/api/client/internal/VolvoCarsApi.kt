package com.github.ayastrebov.volvo.api.client.internal

import com.github.ayastrebov.volvo.api.client.ConnectedVehicle
import com.github.ayastrebov.volvo.api.client.Energy
import com.github.ayastrebov.volvo.api.client.ExtendedVehicle
import com.github.ayastrebov.volvo.api.client.Location
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
internal class VolvoCarsApi(
    private val requester: HttpRequester
) : VolvoCars,
    ConnectedVehicle by ConnectedVehicleApi(requester),
    Energy by EnergyApi(requester),
    ExtendedVehicle by ExtendedVehicleApi(requester),
    Location by LocationApi(requester),
    AutoCloseable by requester