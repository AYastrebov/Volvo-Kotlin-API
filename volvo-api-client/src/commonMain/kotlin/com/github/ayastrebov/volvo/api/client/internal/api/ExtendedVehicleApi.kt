package com.github.ayastrebov.volvo.api.client.internal.api

import com.github.ayastrebov.volvo.api.api.ExtendedVehicle
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester

/**
 * Implementation of the [ExtendedVehicle] API interface.
 *
 * This class is currently a placeholder as the Extended Vehicle API has been deprecated
 * by Volvo and is scheduled for sunset on December 31, 2025. The interface is retained
 * for backwards compatibility but contains no active endpoints.
 *
 * Users should migrate to the [ConnectedVehicle] API which provides equivalent and
 * enhanced functionality for accessing vehicle data.
 *
 * @param requester The HTTP transport layer (unused, retained for interface consistency)
 * @see ExtendedVehicle For deprecation details and migration guidance
 * @see ConnectedVehicle For the recommended replacement API
 */
@Suppress("UnusedPrivateMember")
internal class ExtendedVehicleApi(private val requester: HttpRequester) : ExtendedVehicle