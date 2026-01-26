package com.github.ayastrebov.volvo.api.client.internal.api

import com.github.ayastrebov.volvo.api.InternalVolvoApi
import com.github.ayastrebov.volvo.api.api.ExtendedVehicle
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester

@InternalVolvoApi
internal class ExtendedVehicleApi(private val requester: HttpRequester) : ExtendedVehicle {
}