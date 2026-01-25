package com.github.ayastrebov.volvo.api.client.internal.api

import com.github.ayastrebov.volvo.api.api.Energy
import com.github.ayastrebov.volvo.api.client.internal.extension.requestOptions
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester
import com.github.ayastrebov.volvo.api.client.internal.http.perform
import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.energy.CapabilitiesResponse
import com.github.ayastrebov.volvo.api.model.energy.EnergyStateResponse
import io.ktor.client.request.*

private const val BASE_PATH = "energy/v2/vehicles"

internal class EnergyApi(private val requester: HttpRequester) : Energy {

    override suspend fun getCapabilities(vin: String, requestOptions: RequestOptions?): CapabilitiesResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/capabilities") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getEnergyState(vin: String, requestOptions: RequestOptions?): EnergyStateResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/state") {
                requestOptions(requestOptions)
            }
        }
    }
}