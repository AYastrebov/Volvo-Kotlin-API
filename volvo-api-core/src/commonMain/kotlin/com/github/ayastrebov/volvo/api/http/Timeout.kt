package com.github.ayastrebov.volvo.api.http

import kotlin.time.Duration

/**
 * HTTP operation timeouts.
 *
 * @property request Time period required to process an HTTP call: from sending a request to receiving a response.
 * @property connect Time period in which a client should establish a connection with a server.
 * @property socket Maximum time of inactivity between two data packets when exchanging data with a server.
 */
public data class Timeout(
    public val request: Duration? = null,
    public val connect: Duration? = null,
    public val socket: Duration? = null,
)
