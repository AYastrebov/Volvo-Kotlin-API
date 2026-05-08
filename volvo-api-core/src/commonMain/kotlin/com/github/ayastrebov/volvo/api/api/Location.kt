package com.github.ayastrebov.volvo.api.api

import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.location.LocationResponse

/**
 * Location API.
 *
 * Provides access to the last known location of a connected vehicle.
 *
 * All methods in this interface may throw:
 * @throws com.github.ayastrebov.volvo.api.exception.AuthenticationException if the access token is invalid or expired (401)
 * @throws com.github.ayastrebov.volvo.api.exception.PermissionException if the API key lacks required permissions (403)
 * @throws com.github.ayastrebov.volvo.api.exception.RateLimitException if the rate limit is exceeded (429)
 * @throws com.github.ayastrebov.volvo.api.exception.InvalidRequestException if the request is malformed or the VIN is not found (400/404)
 * @throws com.github.ayastrebov.volvo.api.exception.VolvoServerException if the Volvo API returns a server error (5xx)
 * @throws com.github.ayastrebov.volvo.api.exception.VolvoTimeoutException if the request times out
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
