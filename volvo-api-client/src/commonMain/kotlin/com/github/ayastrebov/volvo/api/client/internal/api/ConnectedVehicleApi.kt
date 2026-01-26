package com.github.ayastrebov.volvo.api.client.internal.api

import com.github.ayastrebov.volvo.api.InternalVolvoApi
import com.github.ayastrebov.volvo.api.api.ConnectedVehicle
import com.github.ayastrebov.volvo.api.client.internal.extension.requestOptions
import com.github.ayastrebov.volvo.api.client.internal.http.HttpRequester
import com.github.ayastrebov.volvo.api.client.internal.http.perform
import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.connectedvehicle.CommandAccessibilityResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.CommandListResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.CommandResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.BrakeStatusResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.DiagnosticsResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.DoorAndLockStatusResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.EmptyCommandRequest
import com.github.ayastrebov.volvo.api.model.connectedvehicle.EngineDiagnosticsResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.EngineStartRequest
import com.github.ayastrebov.volvo.api.model.connectedvehicle.EngineStatusResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.FuelAmountResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.OdometerResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.StatisticsResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.TyreStatusResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.UnlockCommandResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.VehicleDetailsResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.VehicleListResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.WarningsResponse
import com.github.ayastrebov.volvo.api.model.connectedvehicle.WindowStatusResponse
import io.ktor.client.request.*
import io.ktor.http.*

private const val BASE_PATH = "connected-vehicle/v2/vehicles"

@InternalVolvoApi
internal class ConnectedVehicleApi(private val requester: HttpRequester) : ConnectedVehicle {

    // ==================== Vehicle Information ====================

    override suspend fun getVehicleList(requestOptions: RequestOptions?): VehicleListResponse {
        return requester.perform {
            it.get(BASE_PATH) {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getVehicleDetails(vin: String, requestOptions: RequestOptions?): VehicleDetailsResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin") {
                requestOptions(requestOptions)
            }
        }
    }

    // ==================== Status Data ====================

    override suspend fun getWindowStatus(vin: String, requestOptions: RequestOptions?): WindowStatusResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/windows") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getWarnings(vin: String, requestOptions: RequestOptions?): WarningsResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/warnings") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getTyreStatus(vin: String, requestOptions: RequestOptions?): TyreStatusResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/tyres") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getStatistics(vin: String, requestOptions: RequestOptions?): StatisticsResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/statistics") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getOdometer(vin: String, requestOptions: RequestOptions?): OdometerResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/odometer") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getFuelAmount(vin: String, requestOptions: RequestOptions?): FuelAmountResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/fuel") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getEngineDiagnostics(vin: String, requestOptions: RequestOptions?): EngineDiagnosticsResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/engine") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getDoorAndLockStatus(vin: String, requestOptions: RequestOptions?): DoorAndLockStatusResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/doors") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getDiagnostics(vin: String, requestOptions: RequestOptions?): DiagnosticsResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/diagnostics") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getBrakeStatus(vin: String, requestOptions: RequestOptions?): BrakeStatusResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/brakes") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getEngineStatus(vin: String, requestOptions: RequestOptions?): EngineStatusResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/engine-status") {
                requestOptions(requestOptions)
            }
        }
    }

    // ==================== Commands ====================

    override suspend fun getCommandList(vin: String, requestOptions: RequestOptions?): CommandListResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/commands") {
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun getCommandAccessibility(vin: String, requestOptions: RequestOptions?): CommandAccessibilityResponse {
        return requester.perform {
            it.get("$BASE_PATH/$vin/command-accessibility") {
                requestOptions(requestOptions)
            }
        }
    }

    // ==================== Command Invocations ====================

    override suspend fun invokeUnlock(vin: String, requestOptions: RequestOptions?): UnlockCommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/unlock") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeLock(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/lock") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeHonk(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/honk") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeFlash(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/flash") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeHonkFlash(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/honk-flash") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeEngineStop(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/engine-stop") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeEngineStart(
        vin: String,
        request: EngineStartRequest,
        requestOptions: RequestOptions?
    ): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/engine-start") {
                contentType(ContentType.Application.Json)
                setBody(request)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeClimatizationStop(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/climatization-stop") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeClimatizationStart(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/climatization-start") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }

    override suspend fun invokeLockReducedGuard(vin: String, requestOptions: RequestOptions?): CommandResponse {
        return requester.perform {
            it.post("$BASE_PATH/$vin/commands/lock-reduced-guard") {
                contentType(ContentType.Application.Json)
                setBody(EmptyCommandRequest)
                requestOptions(requestOptions)
            }
        }
    }
}