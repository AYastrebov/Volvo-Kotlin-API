package com.github.ayastrebov.volvo.api.api

import com.github.ayastrebov.volvo.api.core.RequestOptions
import com.github.ayastrebov.volvo.api.model.connectedvehicle.*

/**
 * Connected Vehicle API.
 *
 * Provides access to vehicle status, diagnostics, statistics data and remote commands.
 */
public interface ConnectedVehicle {

    // ==================== Vehicle Information ====================

    /**
     * Get list of vehicles associated with the user.
     *
     * @param requestOptions optional request configuration
     * @return list of vehicle VINs
     */
    public suspend fun getVehicleList(
        requestOptions: RequestOptions? = null
    ): VehicleListResponse

    /**
     * Get vehicle details including model, year, fuel type, etc.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return vehicle details
     */
    public suspend fun getVehicleDetails(
        vin: String,
        requestOptions: RequestOptions? = null
    ): VehicleDetailsResponse

    // ==================== Status Data ====================

    /**
     * Get window status for all windows.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return window status
     */
    public suspend fun getWindowStatus(
        vin: String,
        requestOptions: RequestOptions? = null
    ): WindowStatusResponse

    /**
     * Get vehicle warnings (bulb failures, etc.).
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return vehicle warnings
     */
    public suspend fun getWarnings(
        vin: String,
        requestOptions: RequestOptions? = null
    ): WarningsResponse

    /**
     * Get tyre pressure status for all tyres.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return tyre pressure values
     */
    public suspend fun getTyreStatus(
        vin: String,
        requestOptions: RequestOptions? = null
    ): TyreStatusResponse

    /**
     * Get vehicle statistics (average speed, trip meters, etc.).
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return vehicle statistics
     */
    public suspend fun getStatistics(
        vin: String,
        requestOptions: RequestOptions? = null
    ): StatisticsResponse

    /**
     * Get odometer value in kilometers.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return odometer value
     */
    public suspend fun getOdometer(
        vin: String,
        requestOptions: RequestOptions? = null
    ): OdometerResponse

    /**
     * Get fuel amount in liters.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return fuel amount
     */
    public suspend fun getFuelAmount(
        vin: String,
        requestOptions: RequestOptions? = null
    ): FuelAmountResponse

    /**
     * Get engine diagnostic values (oil level, coolant level warnings).
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return engine diagnostics
     */
    public suspend fun getEngineDiagnostics(
        vin: String,
        requestOptions: RequestOptions? = null
    ): EngineDiagnosticsResponse

    /**
     * Get door and lock status.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return door and lock status
     */
    public suspend fun getDoorAndLockStatus(
        vin: String,
        requestOptions: RequestOptions? = null
    ): DoorAndLockStatusResponse

    /**
     * Get vehicle diagnostics (service warnings, washer fluid, etc.).
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return diagnostics values
     */
    public suspend fun getDiagnostics(
        vin: String,
        requestOptions: RequestOptions? = null
    ): DiagnosticsResponse

    /**
     * Get brake status.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return brake status
     */
    public suspend fun getBrakeStatus(
        vin: String,
        requestOptions: RequestOptions? = null
    ): BrakeStatusResponse

    /**
     * Get engine running status.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return engine status
     */
    public suspend fun getEngineStatus(
        vin: String,
        requestOptions: RequestOptions? = null
    ): EngineStatusResponse

    // ==================== Commands ====================

    /**
     * Get list of available commands for the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return list of available commands
     */
    public suspend fun getCommandList(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandListResponse

    /**
     * Get command accessibility status.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command accessibility
     */
    public suspend fun getCommandAccessibility(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandAccessibilityResponse

    // ==================== Command Invocations ====================

    /**
     * Send unlock command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeUnlock(
        vin: String,
        requestOptions: RequestOptions? = null
    ): UnlockCommandResponse

    /**
     * Send lock command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeLock(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send honk command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeHonk(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send flash lights command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeFlash(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send honk and flash command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeHonkFlash(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send engine stop command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeEngineStop(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send engine start command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param request engine start configuration
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeEngineStart(
        vin: String,
        request: EngineStartRequest,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send climatization stop command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeClimatizationStop(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send climatization start command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeClimatizationStart(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse

    /**
     * Send lock with reduced guard command to the vehicle.
     *
     * @param vin Vehicle Identification Number
     * @param requestOptions optional request configuration
     * @return command response
     */
    public suspend fun invokeLockReducedGuard(
        vin: String,
        requestOptions: RequestOptions? = null
    ): CommandResponse
}
