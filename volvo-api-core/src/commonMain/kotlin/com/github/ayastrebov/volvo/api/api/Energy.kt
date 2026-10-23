package com.github.ayastrebov.volvo.api.api

import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.energy.Capabilities
import com.github.ayastrebov.volvo.api.model.energy.EnergyState

/**
 * Energy API.
 *
 * Provides access to the most recent energy state of a vehicle including
 * battery level, charging status, and electric range.
 *
 * Note: unlike the Connected Vehicle and Location APIs, Energy API v2 responses are not
 * wrapped in a `status`/`operationId`/`data` envelope, so these methods return the
 * payload objects directly.
 *
 * All methods in this interface may throw:
 * @throws com.github.ayastrebov.volvo.api.exception.AuthenticationException if the access token is invalid or expired (401)
 * @throws com.github.ayastrebov.volvo.api.exception.PermissionException if the API key lacks required permissions (403)
 * @throws com.github.ayastrebov.volvo.api.exception.RateLimitException if the rate limit is exceeded (429)
 * @throws com.github.ayastrebov.volvo.api.exception.InvalidRequestException if the request is malformed or the VIN is not found (400/404)
 * @throws com.github.ayastrebov.volvo.api.exception.VolvoServerException if the Volvo API returns a server error (5xx)
 * @throws com.github.ayastrebov.volvo.api.exception.VolvoTimeoutException if the request times out
 */
public interface Energy {

    /**
     * Get vehicle capabilities for energy-related endpoints.
     *
     * Provides a summary of the supported endpoints and data points for a vehicle.
     *
     * Required scopes: `openid` and `energy:capability:read`; the per-resource `energy:*` scopes work as well.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return vehicle energy capabilities
     */
    public suspend fun getCapabilities(
        vin: String,
        requestOptions: RequestOptions? = null
    ): Capabilities

    /**
     * Get the latest energy state of the vehicle.
     *
     * Provides the latest energy state data for the vehicle including battery charge level,
     * electric range, charging status, and other energy-related information.
     *
     * Required scopes: `openid` and `energy:state:read`; the per-resource `energy:*` scopes work as well.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return latest energy state
     */
    public suspend fun getEnergyState(
        vin: String,
        requestOptions: RequestOptions? = null
    ): EnergyState
}
