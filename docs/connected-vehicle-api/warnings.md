# Warnings

## Get Warnings

Get vehicle warning indicators for lights and other systems.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/warnings`

### Scopes Required
- `openid`
- `conve:warnings`

### Response

| Field | Description |
|-------|-------------|
| `brakeLightCenterWarning.value` | Center brake light warning |
| `brakeLightCenterWarning.timestamp` | ISO-8601 timestamp |
| `brakeLightLeftWarning.value` | Left brake light warning |
| `brakeLightLeftWarning.timestamp` | ISO-8601 timestamp |
| `brakeLightRightWarning.value` | Right brake light warning |
| `brakeLightRightWarning.timestamp` | ISO-8601 timestamp |
| `fogLightFrontWarning.value` | Front fog light warning |
| `fogLightFrontWarning.timestamp` | ISO-8601 timestamp |
| `fogLightRearWarning.value` | Rear fog light warning |
| `fogLightRearWarning.timestamp` | ISO-8601 timestamp |
| `hazardLightsWarning.value` | Hazard lights warning |
| `hazardLightsWarning.timestamp` | ISO-8601 timestamp |
| `highBeamLeftWarning.value` | Left high beam warning |
| `highBeamLeftWarning.timestamp` | ISO-8601 timestamp |
| `highBeamRightWarning.value` | Right high beam warning |
| `highBeamRightWarning.timestamp` | ISO-8601 timestamp |
| `lowBeamLeftWarning.value` | Left low beam warning |
| `lowBeamLeftWarning.timestamp` | ISO-8601 timestamp |
| `lowBeamRightWarning.value` | Right low beam warning |
| `lowBeamRightWarning.timestamp` | ISO-8601 timestamp |
| `positionLightFrontLeftWarning.value` | Front left position light warning |
| `positionLightFrontRightWarning.value` | Front right position light warning |
| `positionLightRearLeftWarning.value` | Rear left position light warning |
| `positionLightRearRightWarning.value` | Rear right position light warning |
| `turnIndicationFrontLeftWarning.value` | Front left turn indicator warning |
| `turnIndicationFrontRightWarning.value` | Front right turn indicator warning |
| `turnIndicationRearLeftWarning.value` | Rear left turn indicator warning |
| `turnIndicationRearRightWarning.value` | Rear right turn indicator warning |
| `daytimeRunningLightLeftWarning.value` | Left daytime running light warning |
| `daytimeRunningLightRightWarning.value` | Right daytime running light warning |
| `registrationPlateLightWarning.value` | Registration plate light warning |
| `reverseLightsWarning.value` | Reverse lights warning |
| `sideMarkLightsWarning.value` | Side mark lights warning |

### Warning Values

- `NO_WARNING` - No warning
- `FAILURE` - Light or system failure

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/warnings' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "brakeLightCenterWarning": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "lowBeamLeftWarning": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "lowBeamRightWarning": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```
