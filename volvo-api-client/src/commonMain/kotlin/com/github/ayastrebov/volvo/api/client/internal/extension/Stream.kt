package com.github.ayastrebov.volvo.api.client.internal.extension

import com.github.ayastrebov.volvo.api.client.internal.JsonLenient
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.isActive

private const val STREAM_PREFIX = "data:"
private const val STREAM_END_TOKEN = "$STREAM_PREFIX [DONE]"

/**
 * Get data as [Server-Sent Events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events#Event_stream_format).
 */
internal suspend inline fun <reified T> FlowCollector<T>.streamEventsFrom(response: HttpResponse) {
    val channel: ByteReadChannel = response.body()
    try {
        while (currentCoroutineContext().isActive && !channel.isClosedForRead) {
            val line = channel.readLine() ?: continue
            when {
                line.startsWith(STREAM_END_TOKEN) -> break
                line.startsWith(STREAM_PREFIX) -> {
                    val value: T? = try {
                        JsonLenient.decodeFromString(line.removePrefix(STREAM_PREFIX))
                    } catch (_: Exception) {
                        null
                    }
                    if (value != null) emit(value)
                }
            }
        }
    } finally {
        channel.cancel()
    }
}
