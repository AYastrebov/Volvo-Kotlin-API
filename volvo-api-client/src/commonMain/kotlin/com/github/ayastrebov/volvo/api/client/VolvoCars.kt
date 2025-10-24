package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.client.internal.VolvoCarsApi
import com.github.ayastrebov.volvo.api.client.internal.createHttpClient
import com.github.ayastrebov.volvo.api.client.internal.http.HttpTransport
import com.github.ayastrebov.volvo.api.http.Timeout
import io.ktor.client.*
import kotlin.time.Duration.Companion.seconds

public interface VolvoCars: ExtendedVehicle, ConnectedVehicle, Location, Energy, AutoCloseable

public fun VolvoCars(
    apiKey: String,
    token: String,
    logging: LoggingConfig = LoggingConfig(),
    timeout: Timeout = Timeout(socket = 30.seconds),
    headers: Map<String, String> = emptyMap(),
    proxy: ProxyConfig? = null,
    retry: RetryStrategy = RetryStrategy(),
    httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
): VolvoCars = VolvoCars(
    config = VolvoCarsConfig(
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
 * Creates an instance of [VolvoCarsApi].
 *
 * @param config client config
 */
public fun VolvoCars(config: VolvoCarsConfig): VolvoCars {
    val httpClient = createHttpClient(config)
    val transport = HttpTransport(httpClient)
    return VolvoCarsApi(transport)
}