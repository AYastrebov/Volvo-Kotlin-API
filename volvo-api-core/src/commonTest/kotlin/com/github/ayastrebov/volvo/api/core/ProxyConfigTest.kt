package com.github.ayastrebov.volvo.api.core

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertEquals

/**
 * Tests for ProxyConfig configuration classes.
 */
class ProxyConfigTest {

    @Test
    fun httpProxy_storesUrl() {
        val proxy = ProxyConfig.Http("http://proxy.example.com:8080")
        assertEquals("http://proxy.example.com:8080", proxy.url)
    }

    @Test
    fun httpProxy_isProxyConfig() {
        val proxy: ProxyConfig = ProxyConfig.Http("http://proxy.example.com:8080")
        assertIs<ProxyConfig.Http>(proxy)
    }

    @Test
    fun socksProxy_storesHostAndPort() {
        val proxy = ProxyConfig.Socks("socks.example.com", 1080)
        assertEquals("socks.example.com", proxy.host)
        assertEquals(1080, proxy.port)
    }

    @Test
    fun socksProxy_isProxyConfig() {
        val proxy: ProxyConfig = ProxyConfig.Socks("socks.example.com", 1080)
        assertIs<ProxyConfig.Socks>(proxy)
    }

    @Test
    fun proxyConfig_canBeUsedPolymorphically() {
        val httpProxy: ProxyConfig = ProxyConfig.Http("http://proxy:8080")
        val socksProxy: ProxyConfig = ProxyConfig.Socks("socks", 1080)

        val proxies = listOf(httpProxy, socksProxy)
        assertEquals(2, proxies.size)

        when (val proxy = proxies[0]) {
            is ProxyConfig.Http -> assertEquals("http://proxy:8080", proxy.url)
            is ProxyConfig.Socks -> throw AssertionError("Expected Http proxy")
        }

        when (val proxy = proxies[1]) {
            is ProxyConfig.Http -> throw AssertionError("Expected Socks proxy")
            is ProxyConfig.Socks -> {
                assertEquals("socks", proxy.host)
                assertEquals(1080, proxy.port)
            }
        }
    }
}
