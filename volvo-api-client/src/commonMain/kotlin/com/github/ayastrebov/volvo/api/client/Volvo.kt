package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.internal.VolvoApi
import com.github.ayastrebov.volvo.api.client.internal.createHttpClient
import com.github.ayastrebov.volvo.api.client.internal.http.HttpTransport
import com.github.ayastrebov.volvo.api.http.Timeout
import io.ktor.client.*
import kotlin.time.Duration.Companion.seconds

public interface Volvo: ExtendedVehicle, ConnectedVehicle, Location, Energy, AutoCloseable

public fun Volvo(
    apiKey: String,
    token: String,
    logging: LoggingConfig = LoggingConfig(),
    timeout: Timeout = Timeout(socket = 30.seconds),
    headers: Map<String, String> = emptyMap(),
    proxy: ProxyConfig? = null,
    retry: RetryStrategy = RetryStrategy(),
    httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
): Volvo = Volvo(
    config = VolvoConfig(
        apiKey = apiKey,
        token = token,
        logging = logging,
        timeout = timeout,
        headers = headers,
        proxy = proxy,
        retry = retry,
        httpClientConfig = httpClientConfig,
    )
)

/**
 * Creates an instance of [VolvoApi].
 *
 * @param config client config
 */
public fun Volvo(config: VolvoConfig): Volvo {
    val httpClient = createHttpClient(config)
    val transport = HttpTransport(httpClient)
    return VolvoApi(transport)
}