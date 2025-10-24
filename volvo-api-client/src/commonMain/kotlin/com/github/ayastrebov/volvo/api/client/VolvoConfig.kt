package com.github.ayastrebov.volvo.api.client

import com.github.ayastrebov.volvo.api.http.Timeout
import com.github.ayastrebov.volvo.api.logging.LogLevel
import com.github.ayastrebov.volvo.api.logging.Logger
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Volvo API client configuration.
 *
 * @param apiKey VCC Api Key
 * @param token VCC Access Token
 * @param logging client logging configuration
 * @param timeout http client timeout
 * @param headers extra http headers
 * @param proxy HTTP proxy url
 * @param retry rate limit retry configuration
 * @param engine explicit ktor engine for http requests.
 * @param httpClientConfig additional custom client configuration
 */
public class VolvoConfig(
    public val apiKey: String,
    public val token: String,
    public val logging: LoggingConfig = LoggingConfig(),
    public val timeout: Timeout = Timeout(socket = 30.seconds),
    public val headers: Map<String, String> = emptyMap(),
    public val proxy: ProxyConfig? = null,
    public val retry: RetryStrategy = RetryStrategy(),
    public val engine: HttpClientEngine? = null,
    public val httpClientConfig: HttpClientConfig<*>.() -> Unit = {}
)

/** Proxy configuration. */
public sealed interface ProxyConfig {

    /** Creates an HTTP proxy from [url]. */
    public class Http(public val url: String) : ProxyConfig

    /** Create socks proxy from [host] and [port]. */
    public class Socks(public val host: String, public val port: Int) : ProxyConfig
}


/**
 * Specifies the retry strategy
 *
 * @param maxRetries the maximum amount of retries to perform for a request
 * @param base retry base value
 * @param maxDelay max retry delay
 */
public class RetryStrategy(
    public val maxRetries: Int = 3,
    public val base: Double = 2.0,
    public val maxDelay: Duration = 60.seconds,
)

/**
 * Defines the configuration parameters for logging.
 *
 * @property logLevel the level of logging to be used by the HTTP client.
 * @property logger the logger instance to be used by the HTTP client.
 * @property sanitize flag indicating whether to sanitize sensitive information (i.e., authorization header) in the logs
 */
public class LoggingConfig(
    public val logLevel: LogLevel = LogLevel.Headers,
    public val logger: Logger = Logger.Simple,
    public val sanitize: Boolean = true,
)
