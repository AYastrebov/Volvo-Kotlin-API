# Location API

**Version 1** | Base Path: `location/v1/vehicles`

## Overview

The Location API provides access to vehicle location data including GPS coordinates and heading information.

## Features

- Get current vehicle location (latitude, longitude, altitude)
- Get vehicle heading/direction
- GeoJSON Feature format response

## Documentation

- [Get Location](#get-vehicle-location) - Retrieve current vehicle position

## Get Vehicle Location

Retrieve the current location of the vehicle.

**GET** `https://api.volvocars.com/location/v1/vehicles/{vin}/location`

### Scopes Required
- `openid`
- `conve:location`

### Response Format

The response uses [GeoJSON](https://geojson.org/) Feature format.

### Response Fields

| Field | Description |
|-------|-------------|
| `type` | Always `Feature` |
| `geometry.type` | Always `Point` |
| `geometry.coordinates` | Array: `[longitude, latitude, altitude]` |
| `properties.heading` | Vehicle heading in degrees (0-360) |
| `properties.timestamp` | ISO-8601 timestamp of last retrieval |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/location/v1/vehicles/{vin}/location' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "status": 200,
  "operationId": "abc123-def456-ghi789",
  "data": {
    "type": "Feature",
    "geometry": {
      "type": "Point",
      "coordinates": [18.0686, 59.3293, 10.0]
    },
    "properties": {
      "heading": "45",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```

### Coordinate Format

The `coordinates` array follows GeoJSON standard:
- Index 0: **Longitude** (east-west position)
- Index 1: **Latitude** (north-south position)
- Index 2: **Altitude** in meters (optional)

## Availability

### Car Models

This API is available for:
- All cars with **Volvo On Call** add-on from model year 2010-2024
- All cars with **Google Built In** from model year 2020

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
