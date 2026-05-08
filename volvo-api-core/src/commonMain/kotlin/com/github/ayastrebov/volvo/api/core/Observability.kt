package com.github.ayastrebov.volvo.api.core

import kotlin.random.Random

/**
 * Generates a [W3C Trace Context](https://www.w3.org/TR/trace-context/) `traceparent` header value.
 *
 * Volvo APIs support the `traceparent` header for distributed tracing.
 * Include it in [RequestOptions.headers] to correlate requests across systems.
 *
 * Format: `{version}-{trace-id}-{parent-id}-{trace-flags}`
 * - version: `00` (current spec version)
 * - trace-id: 32 hex characters (128-bit random)
 * - parent-id: 16 hex characters (64-bit random)
 * - trace-flags: `01` (sampled)
 *
 * ```kotlin
 * val traceparent = Traceparent.generate()
 * client.getVehicleList(
 *     requestOptions = RequestOptions(
 *         headers = mapOf("traceparent" to traceparent.value)
 *     )
 * )
 * ```
 *
 * @property value The full `traceparent` header string
 * @property traceId The 128-bit trace ID as 32 hex characters
 * @property parentId The 64-bit parent span ID as 16 hex characters
 */
public class Traceparent private constructor(
    public val value: String,
    public val traceId: String,
    public val parentId: String,
) {
    override fun toString(): String = value

    public companion object {
        /**
         * Generates a new `traceparent` header with random trace and span IDs.
         *
         * @param random Random source (override for deterministic testing)
         * @return A new [Traceparent] instance
         */
        public fun generate(random: Random = Random): Traceparent {
            val traceId = randomHex(16, random)
            val parentId = randomHex(8, random)
            return Traceparent(
                value = "00-$traceId-$parentId-01",
                traceId = traceId,
                parentId = parentId,
            )
        }

        private fun randomHex(byteCount: Int, random: Random): String {
            val bytes = random.nextBytes(byteCount)
            return bytes.joinToString("") { it.toUByte().toString(16).padStart(2, '0') }
        }
    }
}
