package com.github.ayastrebov.volvo.api.model.connectedvehicle

import com.github.ayastrebov.volvo.api.model.common.ValueWithTimestamp
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Serialization round-trip tests for Connected Vehicle domain models.
 */
class SerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun vehicleListResponse_serializesAndDeserializes() {
        val original = VehicleListResponse(
            status = 200,
            operationId = "test-op-id",
            data = listOf(VehicleVin("YTEST123456789012"))
        )

        val serialized = json.encodeToString(VehicleListResponse.serializer(), original)
        val deserialized = json.decodeFromString(VehicleListResponse.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.operationId, deserialized.operationId)
        assertEquals(original.data?.size, deserialized.data?.size)
        assertEquals(original.data?.first()?.vin, deserialized.data?.first()?.vin)
    }

    @Test
    fun vehicleDetails_serializesAndDeserializes() {
        val original = VehicleDetails(
            vin = "YTEST123456789012",
            modelYear = 2024,
            gearbox = "AUTOMATIC",
            fuelType = "ELECTRIC",
            externalColour = "White",
            batteryCapacityKWH = 78.0
        )

        val serialized = json.encodeToString(VehicleDetails.serializer(), original)
        val deserialized = json.decodeFromString(VehicleDetails.serializer(), serialized)

        assertEquals(original.vin, deserialized.vin)
        assertEquals(original.modelYear, deserialized.modelYear)
        assertEquals(original.gearbox, deserialized.gearbox)
        assertEquals(original.fuelType, deserialized.fuelType)
        assertEquals(original.batteryCapacityKWH, deserialized.batteryCapacityKWH)
    }

    @Test
    fun valueWithTimestamp_serializesStringValue() {
        val original = ValueWithTimestamp(
            value = "RUNNING",
            timestamp = "2024-01-15T10:30:00Z",
            unit = null
        )

        val serialized = json.encodeToString(ValueWithTimestamp.serializer(kotlinx.serialization.serializer<String>()), original)
        val deserialized = json.decodeFromString(ValueWithTimestamp.serializer(kotlinx.serialization.serializer<String>()), serialized)

        assertEquals(original.value, deserialized.value)
        assertEquals(original.timestamp, deserialized.timestamp)
    }

    @Test
    fun valueWithTimestamp_serializesIntValue() {
        val original = ValueWithTimestamp(
            value = 12500,
            timestamp = "2024-01-15T10:30:00Z",
            unit = "km"
        )

        val serialized = json.encodeToString(ValueWithTimestamp.serializer(kotlinx.serialization.serializer<Int>()), original)
        val deserialized = json.decodeFromString(ValueWithTimestamp.serializer(kotlinx.serialization.serializer<Int>()), serialized)

        assertEquals(original.value, deserialized.value)
        assertEquals(original.timestamp, deserialized.timestamp)
        assertEquals(original.unit, deserialized.unit)
    }

    @Test
    fun valueWithTimestamp_serializesDoubleValue() {
        val original = ValueWithTimestamp(
            value = 45.5,
            timestamp = "2024-01-15T10:30:00Z",
            unit = "liters"
        )

        val serialized = json.encodeToString(ValueWithTimestamp.serializer(kotlinx.serialization.serializer<Double>()), original)
        val deserialized = json.decodeFromString(ValueWithTimestamp.serializer(kotlinx.serialization.serializer<Double>()), serialized)

        assertEquals(original.value, deserialized.value)
        assertEquals(original.timestamp, deserialized.timestamp)
        assertEquals(original.unit, deserialized.unit)
    }

    @Test
    fun engineStartRequest_serializesAndDeserializes() {
        val original = EngineStartRequest(runtimeMinutes = 10)

        val serialized = json.encodeToString(EngineStartRequest.serializer(), original)
        val deserialized = json.decodeFromString(EngineStartRequest.serializer(), serialized)

        assertEquals(original.runtimeMinutes, deserialized.runtimeMinutes)
    }

    @Test
    fun commandResponse_serializesAndDeserializes() {
        val original = CommandResponse(
            status = 200,
            operationId = "test-op-id",
            data = InvokeResult(
                vin = "YTEST123456789012",
                invokeStatus = "COMPLETED",
                message = "Command executed successfully"
            )
        )

        val serialized = json.encodeToString(CommandResponse.serializer(), original)
        val deserialized = json.decodeFromString(CommandResponse.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.operationId, deserialized.operationId)
        assertEquals(original.data?.invokeStatus, deserialized.data?.invokeStatus)
    }

    @Test
    fun unlockCommandResponse_serializesAndDeserializes() {
        val original = UnlockCommandResponse(
            status = 200,
            operationId = "test-op-id",
            data = UnlockInvokeResult(
                vin = "YTEST123456789012",
                invokeStatus = "COMPLETED",
                message = "Unlock completed",
                readyToUnlock = true,
                readyToUnlockUntil = 1705312200000
            )
        )

        val serialized = json.encodeToString(UnlockCommandResponse.serializer(), original)
        val deserialized = json.decodeFromString(UnlockCommandResponse.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.data?.readyToUnlock, deserialized.data?.readyToUnlock)
        assertEquals(original.data?.readyToUnlockUntil, deserialized.data?.readyToUnlockUntil)
    }

    @Test
    fun commandAccessibility_serializesAndDeserializes() {
        val original = CommandAccessibilityResponse(
            status = 200,
            operationId = "test-op-id",
            data = CommandAccessibility(
                availableCommands = listOf(
                    AvailableCommand(command = "LOCK", href = "/commands/lock")
                ),
                unavailableCommands = listOf(
                    UnavailableCommand(command = "ENGINE_START", reason = "NOT_SUPPORTED")
                )
            )
        )

        val serialized = json.encodeToString(CommandAccessibilityResponse.serializer(), original)
        val deserialized = json.decodeFromString(CommandAccessibilityResponse.serializer(), serialized)

        assertEquals(original.data?.availableCommands?.size, deserialized.data?.availableCommands?.size)
        assertEquals(original.data?.unavailableCommands?.size, deserialized.data?.unavailableCommands?.size)
    }
}
