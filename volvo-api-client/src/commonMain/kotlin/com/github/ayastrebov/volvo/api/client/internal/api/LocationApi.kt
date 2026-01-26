package com.github.ayastrebov.volvo.api.client.internal.api

import com.github.ayastrebov.volvo.api.InternalVolvoApi
import com.github.ayastrebov.volvo.api.api.Location
import com.github.ayastrebov.volvo.api.client.internal.extension.requestOptions
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester
import com.github.ayastrebov.volvo.api.client.internal.http.perform
import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.location.LocationResponse
import io.ktor.client.request.*

private const val BASE_PATH = "location/v1/vehicles"

@InternalVolvoApi
internal class LocationApi(private val requester: HttpRequester) : Location {

    override suspend fun getVehicleLocation(vin: String, requestOptions: RequestOptions?): LocationResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/location") {
                requestOptions(requestOptions)
            }
        }
    }
}