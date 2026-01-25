# Connected Vehicle API

**Version 2** | Base Path: `connected-vehicle/v2/vehicles`

## Overview

The Connected Vehicle API allows your application to receive data from vehicles and send commands. It includes vehicle status, diagnostics, statistics, and metadata for a complete end-user experience.

## Features

### Read Operations
- Vehicle details and metadata
- Brake status
- Diagnostics
- Engine status
- Fuel amount
- Window status
- Tyre pressures
- Warnings
- Odometer
- Vehicle statistics
- Door and lock status

### Commands
- Lock / Unlock doors
- Lock with reduced guard
- Climate start / stop
- Flash lights
- Sound horn
- Engine start / stop

## Documentation

- [API Details](details.md) - Headers, status codes, error handling
- [Vehicle Endpoints](vehicle.md) - List vehicles, get vehicle details
- [Commands](commands.md) - Available commands and accessibility
- [Doors, Windows & Locks](doors-windows-locks.md) - Status and control
- [Engine](engine.md) - Status and start/stop commands
- [Diagnostics](diagnostics.md) - Vehicle diagnostics
- [Climate](climate.md) - Climate control commands
- [Fuel](fuel.md) - Fuel level information
- [Lights & Sound](lights-sound.md) - Flash and honk commands
- [Odometer](odometer.md) - Mileage data
- [Statistics](statistics.md) - Trip and consumption statistics
- [Tyres](tyres.md) - Tyre pressure status
- [Warnings](warnings.md) - Vehicle warning indicators

## Availability

### Car Models

This API is available for:
- All cars with **Volvo On Call** add-on from model year 2010-2024
- All cars with **Google Built In** from model year 2020

Some endpoints have limited availability. Use [List commands](commands.md) to check availability for a specific vehicle.

### Regions

| Region | Status |
|--------|--------|
| Europe / Middle East / Africa | Available |
| US / Canada / Latin America | Available |
| Other regions | Coming soon |

Test credentials work globally.

## Rate Limits

| Endpoint Type | Limit |
|--------------|-------|
| Standard endpoints | 100 requests/minute |
| Invocation commands | 10 requests/minute |

Limits are per Volvo ID + Client ID combination. Exceeding limits returns HTTP `429`.

## OpenAPI Specification

[Download the OpenAPI specification](https://developer.volvocars.com) as a JSON file.

> **Migration Notice**: If you're using the Consumer API (vocmo), migrate to the Connected Vehicle API as soon as possible. The Consumer API is deprecated and will be removed.
