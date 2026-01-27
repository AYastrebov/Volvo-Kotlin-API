package com.github.ayastrebov.volvo.api.client.internal.extension

import com.github.ayastrebov.volvo.api.client.internal.JsonLenient
import kotlinx.serialization.json.*

/**
 * Converts a serializable object to a JSON element with streaming enabled.
 *
 * This function serializes the given object to JSON and adds a `"stream": true`
 * property to the resulting JSON object. This is used for API endpoints that
 * support streaming responses, where the server sends data incrementally rather
 * than all at once.
 *
 * @param T The type of the object to serialize (must be serializable)
 * @param serializable The object to convert to a streaming request JSON
 * @return A [JsonElement] containing the serialized object with `"stream": true` added
 */
internal inline fun <reified T> streamRequestOf(serializable: T): JsonElement {
    val enableStream = "stream" to JsonPrimitive(true)
    val json = JsonLenient.encodeToJsonElement(serializable)
    val map = json.jsonObject.toMutableMap().also { it += enableStream }
    return JsonObject(map)
}