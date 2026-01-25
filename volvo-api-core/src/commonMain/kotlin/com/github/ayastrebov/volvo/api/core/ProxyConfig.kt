package com.github.ayastrebov.volvo.api.core

/**
 * Proxy configuration for HTTP client.
 */
public sealed interface ProxyConfig {

    /**
     * HTTP proxy configuration.
     *
     * @param url The HTTP proxy URL (e.g., "http://proxy.example.com:8080")
     */
    public class Http(public val url: String) : ProxyConfig

    /**
     * SOCKS proxy configuration.
     *
     * @param host The SOCKS proxy host
     * @param port The SOCKS proxy port
     */
    public class Socks(public val host: String, public val port: Int) : ProxyConfig
}
