# Energy State

## Get Energy State

Retrieves the latest energy state of the vehicle including battery level, charging status, and range information.

**GET** `https://api.volvocars.com/energy/v2/vehicles/{vin}/state`

### Scopes Required
- `openid`
- `energy:state:read`

### Response Fields

Each field includes:
- `status` - Request status (`OK`, `ERROR`)
- `value` - The data value
- `updatedAt` - ISO-8601 timestamp of last retrieval
- `unit` - Unit of measurement (where applicable)

| Field | Description | Values/Units |
|-------|-------------|--------------|
| `batteryChargeLevel` | Current battery charge level | `%` |
| `targetBatteryChargeLevel` | Target charge level setting | `%` |
| `electricRange` | Estimated electric range | `km` |
| `chargerConnectionStatus` | Charger connection status | `CONNECTED`, `DISCONNECTED` |
| `chargingStatus` | Current charging status | `CHARGING`, `NOT_CHARGING`, `CHARGING_PAUSED` |
| `chargingType` | Type of charging | `AC`, `DC` |
| `chargingPower` | Current charging power | `kW` |
| `chargerPowerStatus` | Charger power status | `OK`, `REDUCED` |
| `chargingCurrentLimit` | Charging current limit | `A` |
| `estimatedChargingTimeToTargetBatteryChargeLevel` | Estimated time to reach target | `minutes` |

### Status Values

- `OK` - Data successfully retrieved
- `ERROR` - Unable to retrieve data (check [Capabilities](capabilities.md))

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/energy/v2/vehicles/{vin}/state' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "batteryChargeLevel": {
      "status": "OK",
      "value": 78.5,
      "updatedAt": "2024-01-15T10:30:00Z",
      "unit": "%"
    },
    "targetBatteryChargeLevel": {
      "status": "OK",
      "value": 80,
      "updatedAt": "2024-01-15T10:30:00Z",
      "unit": "%"
    },
    "electricRange": {
      "status": "OK",
      "value": 350,
      "updatedAt": "2024-01-15T10:30:00Z",
      "unit": "km"
    },
    "chargerConnectionStatus": {
      "status": "OK",
      "value": "CONNECTED",
      "updatedAt": "2024-01-15T10:30:00Z"
    },
    "chargingStatus": {
      "status": "OK",
      "value": "CHARGING",
      "updatedAt": "2024-01-15T10:30:00Z"
    },
    "chargingType": {
      "status": "OK",
      "value": "AC",
      "updatedAt": "2024-01-15T10:30:00Z"
    },
    "chargingPower": {
      "status": "OK",
      "value": 11.0,
      "updatedAt": "2024-01-15T10:30:00Z",
      "unit": "kW"
    },
    "estimatedChargingTimeToTargetBatteryChargeLevel": {
      "status": "OK",
      "value": 45,
      "updatedAt": "2024-01-15T10:30:00Z",
      "unit": "minutes"
    }
  }
}
```
