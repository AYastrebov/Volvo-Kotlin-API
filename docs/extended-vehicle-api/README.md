# Extended Vehicle API

**Version 1** | Base Path: `extended-vehicle/v1/vehicles`

> **DEPRECATION NOTICE**: This API will be shut down on **December 31, 2025**. All endpoints are available via the [Connected Vehicle API](../connected-vehicle-api/README.md). Please migrate as soon as possible.

## Overview

The Extended Vehicle API allows you to get latest vehicle status information remotely. This API is an implementation of [ISO 20078-1](https://www.iso.org/standard/66978.html) and is therefore limited in data and features.

For full functionality, use the [Connected Vehicle API](../connected-vehicle-api/README.md).

## Features

- Brake status
- Diagnostics
- Doors status
- Engine status
- Fuel status
- Lock status
- Odometer status
- Service information
- Trip information
- Tyre status
- Vehicle statistics
- Warnings
- Windows status

## Required Headers

All requests require these headers:

| Header | Description |
|--------|-------------|
| `accept` | `application/json` |
| `authorization` | `Bearer <your-access-token>` |
| `vcc-api-key` | `<your-vcc-api-key>` |

## Availability

### Car Models

This API is available for:
- All cars with **Volvo On Call** add-on from model year 2010-2024
- All cars with **Google Built In** from model year 2020

Some endpoints have limited availability.

### Regions

| Region | Status |
|--------|--------|
| Europe / Middle East / Africa | Available |
| Other regions | Not available |

Test credentials work globally.

## Rate Limits

| Endpoint Type | Limit |
|--------------|-------|
| All endpoints | 100 requests/minute |

Limits are per Volvo ID + Client ID combination. Exceeding limits returns HTTP `429`.

## Migration Guide

All Extended Vehicle API functionality is available in the Connected Vehicle API:

| Extended Vehicle Endpoint | Connected Vehicle Equivalent |
|--------------------------|------------------------------|
| `/brakes` | `/brakes` |
| `/diagnostics` | `/diagnostics` |
| `/doors` | `/doors` |
| `/engine-status` | `/engine-status` |
| `/fuel` | `/fuel` |
| `/odometer` | `/odometer` |
| `/statistics` | `/statistics` |
| `/tyres` | `/tyres` |
| `/warnings` | `/warnings` |
| `/windows` | `/windows` |

See the [Connected Vehicle API documentation](../connected-vehicle-api/README.md) for endpoint details.

## OpenAPI Specification

[Download the OpenAPI specification](https://developer.volvocars.com) as a JSON file.
