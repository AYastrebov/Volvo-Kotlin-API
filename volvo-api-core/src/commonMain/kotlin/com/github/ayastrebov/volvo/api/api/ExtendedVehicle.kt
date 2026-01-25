package com.github.ayastrebov.volvo.api.api

/**
 * Extended Vehicle API interface.
 *
 * @deprecated The Extended Vehicle API is deprecated and will be sunset on December 31, 2025.
 * Please migrate to the Connected Vehicle API for all vehicle data needs.
 * See: https://developer.volvocars.com/apis/connected-vehicle/
 */
@Deprecated(
    message = "Extended Vehicle API is deprecated (sunset Dec 31, 2025). Use Connected Vehicle API instead.",
    replaceWith = ReplaceWith("ConnectedVehicle"),
    level = DeprecationLevel.WARNING
)
public interface ExtendedVehicle
