# Module volvo-api-client

Kotlin Multiplatform HTTP client implementation for Volvo Vehicle APIs.

This module provides the HTTP client implementation using Ktor for communicating with Volvo APIs:

- VolvoCars: Main entry point for API operations
- VolvoCarsConfig: Client configuration (API key, token, timeouts, proxy, retry)
- HTTP Transport: Ktor-based HTTP layer with automatic error handling and retry

Supported platforms: JVM, Android, iOS, macOS, Linux, Windows, JavaScript (Node.js), WebAssembly.

# Package com.github.ayastrebov.volvo.api.client

Main client classes for interacting with Volvo APIs. Contains VolvoCars (the main API client implementing ConnectedVehicle, Energy, ExtendedVehicle, and Location interfaces) and VolvoCarsConfig (client configuration with support for logging, timeouts, retry, and proxy).
