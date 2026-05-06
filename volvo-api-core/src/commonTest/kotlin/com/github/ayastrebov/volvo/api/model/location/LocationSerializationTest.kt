package com.github.ayastrebov.volvo.api.model.location

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Serialization round-trip tests for Location domain models.
 */
class LocationSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun point_serializesAndDeserializes() {
        val original = Point(
            type = "Point",
            coordinates = listOf(18.0686, 59.3293, 10.0)
        )

        val serialized = json.encodeToString(Point.serializer(), original)
        val deserialized = json.decodeFromString(Point.serializer(), serialized)

        assertEquals(original.type, deserialized.type)
        assertEquals(original.coordinates?.size, deserialized.coordinates?.size)
        assertEquals(original.coordinates?.get(0), deserialized.coordinates?.get(0))
    }

    @Test
    fun point_helperProperties_returnCorrectValues() {
        val point = Point(
            type = "Point",
            coordinates = listOf(18.0686, 59.3293, 10.0)
        )

        assertEquals(18.0686, point.longitude)
        assertEquals(59.3293, point.latitude)
        assertEquals(10.0, point.altitude)
    }

    @Test
    fun point_helperProperties_returnNullForMissingCoordinates() {
        val point = Point(type = "Point", coordinates = null)

        assertEquals(null, point.longitude)
        assertEquals(null, point.latitude)
        assertEquals(null, point.altitude)
    }

    @Test
    fun point_helperProperties_returnNullForMissingAltitude() {
        val point = Point(
            type = "Point",
            coordinates = listOf(18.0686, 59.3293)
        )

        assertEquals(18.0686, point.longitude)
        assertEquals(59.3293, point.latitude)
        assertEquals(null, point.altitude)
    }

    @Test
    fun feature_serializesAndDeserializes() {
        val original = Feature(
            type = "Feature",
            properties = mapOf("timestamp" to "2024-01-15T10:30:00Z"),
            geometry = Point(
                type = "Point",
                coordinates = listOf(18.0686, 59.3293)
            )
        )

        val serialized = json.encodeToString(Feature.serializer(), original)
        val deserialized = json.decodeFromString(Feature.serializer(), serialized)

        assertEquals(original.type, deserialized.type)
        assertEquals(original.properties?.get("timestamp"), deserialized.properties?.get("timestamp"))
        val geometry = deserialized.geometry
        assertNotNull(geometry)
        assertEquals(original.geometry?.longitude, geometry.longitude)
    }

    @Test
    fun locationResponse_serializesAndDeserializes() {
        val original = LocationResponse(
            status = 200,
            operationId = "test-op-id",
            data = Feature(
                type = "Feature",
                properties = mapOf("heading" to "45"),
                geometry = Point(
                    type = "Point",
                    coordinates = listOf(18.0686, 59.3293, 10.0)
                )
            )
        )

        val serialized = json.encodeToString(LocationResponse.serializer(), original)
        val deserialized = json.decodeFromString(LocationResponse.serializer(), serialized)

        assertEquals(original.status, deserialized.status)
        assertEquals(original.operationId, deserialized.operationId)
        assertEquals(original.data?.type, deserialized.data?.type)
        assertEquals(original.data?.geometry?.longitude, deserialized.data?.geometry?.longitude)
        assertEquals(original.data?.geometry?.latitude, deserialized.data?.geometry?.latitude)
    }
}
