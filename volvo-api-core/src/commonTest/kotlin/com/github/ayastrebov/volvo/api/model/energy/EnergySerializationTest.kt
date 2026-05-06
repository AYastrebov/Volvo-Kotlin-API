package com.github.ayastrebov.volvo.api.model.energy

import kotlin.time.Instant
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Serialization round-trip tests for Energy domain models.
 */
class EnergySerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun resourceResultString_serializesAndDeserializes() {
        val original = ResourceResultString(
            status = "OK",
            value = "CHARGING",
            updatedAt = Instant.parse("2024-01-15T10:30:00Z")
        )

        val serialized = json.encodeToString(ResourceResultString.serializer(), original)
        val deserialized = json.decodeFromString(ResourceResultString.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.value, deserialized.value)
        assertEquals(original.updatedAt, deserialized.updatedAt)
    }

    @Test
    fun resourceResultIntegerWithUnit_serializesAndDeserializes() {
        val original = ResourceResultIntegerWithUnit(
            status = "OK",
            value = 350,
            updatedAt = Instant.parse("2024-01-15T10:30:00Z"),
            unit = "km"
        )

        val serialized = json.encodeToString(ResourceResultIntegerWithUnit.serializer(), original)
        val deserialized = json.decodeFromString(ResourceResultIntegerWithUnit.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.value, deserialized.value)
        assertEquals(original.unit, deserialized.unit)
    }

    @Test
    fun resourceResultFloatWithUnit_serializesAndDeserializes() {
        val original = ResourceResultFloatWithUnit(
            status = "OK",
            value = 78.5f,
            updatedAt = Instant.parse("2024-01-15T10:30:00Z"),
            unit = "%"
        )

        val serialized = json.encodeToString(ResourceResultFloatWithUnit.serializer(), original)
        val deserialized = json.decodeFromString(ResourceResultFloatWithUnit.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.value, deserialized.value)
        assertEquals(original.unit, deserialized.unit)
    }

    @Test
    fun energyState_serializesAndDeserializes() {
        val original = EnergyState(
            batteryChargeLevel = ResourceResultFloatWithUnit(
                status = "OK",
                value = 78.5f,
                unit = "%"
            ),
            electricRange = ResourceResultIntegerWithUnit(
                status = "OK",
                value = 350,
                unit = "km"
            ),
            chargingStatus = ResourceResultString(
                status = "OK",
                value = "CHARGING"
            )
        )

        val serialized = json.encodeToString(EnergyState.serializer(), original)
        val deserialized = json.decodeFromString(EnergyState.serializer(), serialized)

        assertEquals(original.batteryChargeLevel?.value, deserialized.batteryChargeLevel?.value)
        assertEquals(original.electricRange?.value, deserialized.electricRange?.value)
        assertEquals(original.chargingStatus?.value, deserialized.chargingStatus?.value)
    }

    @Test
    fun capabilities_serializesAndDeserializes() {
        val original = Capabilities(
            getEnergyState = GetEnergyStateCapability(
                isSupported = true,
                batteryChargeLevel = Capability(isSupported = true),
                electricRange = Capability(isSupported = true)
            )
        )

        val serialized = json.encodeToString(Capabilities.serializer(), original)
        val deserialized = json.decodeFromString(Capabilities.serializer(), serialized)

        val energyState = deserialized.getEnergyState
        assertNotNull(energyState)
        assertEquals(true, energyState.isSupported)
        assertEquals(true, energyState.batteryChargeLevel?.isSupported)
    }

    @Test
    fun energyStateResponse_serializesAndDeserializes() {
        val original = EnergyStateResponse(
            status = 200,
            operationId = "test-op-id",
            data = EnergyState(
                batteryChargeLevel = ResourceResultFloatWithUnit(
                    status = "OK",
                    value = 78.5f,
                    unit = "%"
                )
            )
        )

        val serialized = json.encodeToString(EnergyStateResponse.serializer(), original)
        val deserialized = json.decodeFromString(EnergyStateResponse.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.operationId, deserialized.operationId)
        assertEquals(original.data?.batteryChargeLevel?.value, deserialized.data?.batteryChargeLevel?.value)
    }
}
