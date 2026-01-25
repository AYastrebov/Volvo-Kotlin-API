package com.github.ayastrebov.volvo.api.api

import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.location.LocationResponse

/**
 * Location API.
 *
 * Provides access to the last known location of a connected vehicle.
 */
public interface Location {

    /**
     * Get the last known location of the vehicle.
     *
     * Returns a GeoJSON Feature containing the vehicle's coordinates.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return location response with GeoJSON data
     */
    public suspend fun getVehicleLocation(
        vin: String,
        requestOptions: RequestOptions? = null
    ): LocationResponse
}
