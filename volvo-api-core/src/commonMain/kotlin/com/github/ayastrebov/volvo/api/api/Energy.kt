package com.github.ayastrebov.volvo.api.api

import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.energy.CapabilitiesResponse
import com.github.ayastrebov.volvo.api.model.energy.EnergyStateResponse

/**
 * Energy API.
 *
 * Provides access to the most recent energy state of a vehicle including
 * battery level, charging status, and electric range.
 */
public interface Energy {

    /**
     * Get vehicle capabilities for energy-related endpoints.
     *
     * Provides a summary of the supported endpoints and data points for a vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return capabilities response
     */
    public suspend fun getCapabilities(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CapabilitiesResponse

    /**
     * Get the latest energy state of the vehicle.
     *
     * Provides the latest energy state data for the vehicle including battery charge level,
     * electric range, charging status, and other energy-related information.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return energy state response
     */
    public suspend fun getEnergyState(
        vin: String,
        requestOptions: RequestOptions? = null
    ): EnergyStateResponse
}
