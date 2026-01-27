package com.github.ayastrebov.volvo.api.client.internal.api

import com.github.ayastrebov.volvo.api.api.Location
import com.github.ayastrebov.volvo.api.client.internal.extension.requestOptions
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester
import com.github.ayastrebov.volvo.api.client.internal.http.perform
import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.location.LocationResponse
import io.ktor.client.request.*

private const val BASE_PATH = "location/v1/vehicles"

/**
 * Implementation of the [Location] API interface.
 *
 * This class provides access to the Location API v1 endpoint for retrieving
 * the current geographic position of a vehicle, including latitude, longitude,
 * heading, and timestamp information.
 *
 * All methods delegate HTTP operations to the shared [HttpRequester] and apply
 * optional [RequestOptions] for per-request customization.
 *
 * @param requester The HTTP transport layer for executing API requests
 * @see Location For the public interface definition and method documentation
 */
internal class LocationApi(private val requester: HttpRequester) : Location {

    override suspend fun getVehicleLocation(vin: String, requestOptions: RequestOptions?): LocationResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/location") {
                requestOptions(requestOptions)
            }
        }
    }
}