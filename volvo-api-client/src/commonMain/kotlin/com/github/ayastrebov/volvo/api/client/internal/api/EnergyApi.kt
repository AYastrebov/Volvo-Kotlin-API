package com.github.ayastrebov.volvo.api.client.internal.api

import com.github.ayastrebov.volvo.api.api.Energy
import com.github.ayastrebov.volvo.api.client.internal.extension.requestOptions
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester
import com.github.ayastrebov.volvo.api.client.internal.http.perform
import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.energy.Capabilities
import com.github.ayastrebov.volvo.api.model.energy.EnergyState
import io.ktor.client.request.*

private const val BASE_PATH = "energy/v2/vehicles"

/**
 * Implementation of the [Energy] API interface.
 *
 * This class provides access to the Energy API v2 endpoints for electric and hybrid vehicles,
 * enabling retrieval of:
 * - Vehicle energy capabilities (battery capacity, charging support)
 * - Current energy state (battery level, charging status, estimated range)
 *
 * Energy API v2 returns the payload directly, without the `status`/`operationId`/`data`
 * envelope used by the other Volvo APIs, so responses deserialize straight into the models.
 *
 * All methods delegate HTTP operations to the shared [HttpRequester] and apply
 * optional [RequestOptions] for per-request customization.
 *
 * @param requester The HTTP transport layer for executing API requests
 * @see Energy For the public interface definition and method documentation
 */
internal class EnergyApi(private val requester: HttpRequester) : Energy {

    override suspend fun getCapabilities(vin: String, requestOptions: RequestOptions?): Capabilities {
        return requester.perform {
            it.get("$BASE_PATH/$vin/capabilities") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getEnergyState(vin: String, requestOptions: RequestOptions?): EnergyState {
        return requester.perform {
            it.get("$BASE_PATH/$vin/state") {
                requestOptions(requestOptions)
            }
        }
    }
}
