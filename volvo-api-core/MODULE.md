# Module volvo-api-core

Core module for the Volvo Kotlin API library containing public interfaces, data models, and exception hierarchy.

This module provides the foundational types used by the Volvo API client:

- API Interfaces for Connected Vehicle, Energy, Extended Vehicle, and Location APIs
- Data Models for request and response handling
- Exception Hierarchy for typed error scenarios (RateLimitException, AuthenticationException, PermissionException, InvalidRequestException, VolvoServerException)
- Configuration for logging, proxy, retry, and timeout settings

# Package com.github.ayastrebov.volvo.api.api

Public API interfaces defining available operations for Volvo vehicle APIs including ConnectedVehicle, Energy, ExtendedVehicle, and Location.

# Package com.github.ayastrebov.volvo.api.core

Configuration classes for customizing client behavior including RequestOptions, RetryStrategy, LoggingConfig, and ProxyConfig.

# Package com.github.ayastrebov.volvo.api.exception

Exception hierarchy for handling Volvo API errors with typed exceptions for rate limiting (429), authentication (401), permissions (403), invalid requests (400/404/409/415), and server errors (5xx).

# Package com.github.ayastrebov.volvo.api.http

HTTP-related types including Timeout configuration for socket, connect, and request timeouts.

# Package com.github.ayastrebov.volvo.api.logging

Logging configuration for HTTP client with support for different log levels (All, Headers, Body, Info, None) and loggers (Default, Simple, Empty).

# Package com.github.ayastrebov.volvo.api.model

Data models for Volvo API requests and responses including vehicle details, energy state, location, and command responses.
