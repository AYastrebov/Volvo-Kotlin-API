# Volvo Cars API Documentation

This documentation covers the Volvo Cars APIs available through the [Volvo Cars Developer Portal](https://developer.volvocars.com/apis/).

## Table of Contents

- [Getting Started](getting-started.md)
- [Authorization](authorization.md)
- [Test Access Tokens](test-access-tokens.md)

### APIs

- **[Connected Vehicle API](connected-vehicle-api/README.md)** - Vehicle data and remote commands
- **[Energy API](energy-api/README.md)** - Electric/hybrid vehicle energy state
- **[Location API](location-api/README.md)** - Vehicle location data
- **[Extended Vehicle API](extended-vehicle-api/README.md)** - *(Deprecated)* ISO 20078-1 implementation

## Quick Reference

| API | Base Path | Version |
|-----|-----------|---------|
| Connected Vehicle | `connected-vehicle/v2/vehicles` | v2 |
| Energy | `energy/v2/vehicles` | v2 |
| Location | `location/v1/vehicles` | v1 |
| Extended Vehicle | `extended-vehicle/v1/vehicles` | v1 (Deprecated) |

**Base URL**: `https://api.volvocars.com`

## Authentication

All APIs require:
- **VCC API Key** - Obtained by creating an application in the Developer Portal
- **OAuth2 Access Token** - End-user authorization via Volvo ID

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

## Rate Limits

- **Standard endpoints**: 100 requests per minute (per Volvo ID + Client ID combination)
- **Command invocation endpoints**: 10 requests per minute

Exceeding limits returns HTTP `429 Too Many Requests`.

## Regional Availability

APIs are available for vehicles in:
- **Europe / Middle East / Africa**
- **US / Canada / Latin America**

Test credentials work globally.
