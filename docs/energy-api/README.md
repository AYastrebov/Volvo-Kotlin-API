# Energy API

**Version 2** | Base Path: `energy/v2/vehicles`

## Overview

The Energy API provides access to the most recent energy state of electric and plug-in hybrid vehicles.

## Features

### Energy State Endpoint
Retrieves information about the latest energy state of the vehicle:
- Battery charge level
- Target battery charge level
- Charging connection status
- Charging system status
- Charging type
- Charging power
- Charger power status
- Charging current limit
- Estimated charging time
- Electric range

### Capabilities Endpoint
Retrieves information about the capabilities of the vehicle.

## Documentation

- [API Details](details.md) - Headers, status codes, error handling
- [Energy State](state.md) - Battery and charging information
- [Capabilities](capabilities.md) - Vehicle capability discovery

## Availability

### Car Models

This API works for all plug-in hybrid and electric vehicles equipped with **Google Automotive System (Google built-in)**.

**Full Support (BEV - Battery Electric Vehicles)**:
- EC40/C40
- EX40/XC40 BEV
- EX30
- EX90
- ES90

**Full Support (PHEV - Plug-in Hybrid Electric Vehicles)**:
- XC60 PHEV, S90 PHEV, V90 PHEV from model year 2022
- XC90 PHEV, S60 PHEV, V60 PHEV from model year 2023

**Limited Support (PHEV Classic without Google built-in)**:
- XC40 PHEV
- XC60 PHEV, S90 PHEV, V90 PHEV including model year 2021
- XC90 PHEV, S60 PHEV, V60 PHEV including model year 2022

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
| All endpoints | 100 requests/minute |

Limits are per Volvo ID + Client ID combination. Exceeding limits returns HTTP `429`.

## OpenAPI Specification

[Download the OpenAPI specification](https://developer.volvocars.com) as a JSON file.
