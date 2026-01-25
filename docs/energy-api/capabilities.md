# Capabilities

## Get Capabilities

Provides a summary of supported endpoints and data points for a vehicle. Each endpoint has a capability representation indicating whether it's available.

**GET** `https://api.volvocars.com/energy/v2/vehicles/{vin}/capabilities`

### Scopes Required
- `openid`
- `energy:capability:read`

### Response Structure

The response body is a map of capabilities where each capability includes:
- `isSupported` - Boolean flag indicating support

Capabilities may contain nested (child) capabilities. For example, `getEnergyState` may be true, but its child `chargingPower` can be false, meaning when calling `/state`, the `chargingPower.status` will be `ERROR` with code `NOT_SUPPORTED`.

### Response Fields

#### GetEnergyStateCapability

| Field | Description |
|-------|-------------|
| `isSupported` | Whether `/state` endpoint is supported |
| `batteryChargeLevel.isSupported` | Read current battery charge level |
| `electricRange.isSupported` | Read estimated electric range |
| `chargerConnectionStatus.isSupported` | Read charger connection status |
| `chargingStatus.isSupported` | Read charging status |
| `chargingType.isSupported` | Read charging type |
| `chargerPowerStatus.isSupported` | Read charger power status |
| `estimatedChargingTimeToTargetBatteryChargeLevel.isSupported` | Read estimated charging time |
| `targetBatteryChargeLevel.isSupported` | Read target battery charge level |
| `chargingCurrentLimit.isSupported` | Read charging current limit |
| `chargingPower.isSupported` | Read current charging power |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/energy/v2/vehicles/{vin}/capabilities' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "getEnergyState": {
    "isSupported": true,
    "batteryChargeLevel": {
      "isSupported": true
    },
    "electricRange": {
      "isSupported": true
    },
    "chargerConnectionStatus": {
      "isSupported": true
    },
    "chargingStatus": {
      "isSupported": true
    },
    "chargingType": {
      "isSupported": true
    },
    "chargerPowerStatus": {
      "isSupported": true
    },
    "estimatedChargingTimeToTargetBatteryChargeLevel": {
      "isSupported": true
    },
    "targetBatteryChargeLevel": {
      "isSupported": true
    },
    "chargingCurrentLimit": {
      "isSupported": true
    },
    "chargingPower": {
      "isSupported": false
    }
  }
}
```
