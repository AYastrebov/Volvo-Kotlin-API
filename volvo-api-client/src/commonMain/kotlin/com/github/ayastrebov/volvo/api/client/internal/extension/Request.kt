package com.github.ayastrebov.volvo.api.client.internal.extension

import com.github.ayastrebov.volvo.api.client.internal.JsonLenient
import kotlinx.serialization.json.*

internal inline fun <reified T> streamRequestOf(serializable: T): JsonElement {
    val enableStream = "stream" to JsonPrimitive(true)
    val json = JsonLenient.encodeToJsonElement(serializable)
    val map = json.jsonObject.toMutableMap().also { it += enableStream }
    return JsonObject(map)
}