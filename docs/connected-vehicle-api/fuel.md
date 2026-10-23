# Fuel

## Get Fuel Amount

Get the vehicle's current fuel level and, for hybrids and electric vehicles, the battery charge level.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/fuel`

### Scopes Required
- `openid`
- `conve:fuel_status`
- `conve:battery_charge_level` (for `batteryChargeLevel`)

### Response

| Field | Description |
|-------|-------------|
| `fuelAmount.value` | Current fuel amount |
| `fuelAmount.unit` | Unit of measurement (`l`) |
| `fuelAmount.timestamp` | ISO-8601 timestamp of last retrieval |
| `batteryChargeLevel.value` | High-voltage battery charge level. Returned by the live API; not in the published specification |
| `batteryChargeLevel.unit` | Unit of measurement (`%`) |
| `batteryChargeLevel.timestamp` | ISO-8601 timestamp of last retrieval |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/fuel' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "batteryChargeLevel": {
      "value": 42.0,
      "unit": "%",
      "timestamp": "2026-10-23T18:36:45Z"
    },
    "fuelAmount": {
      "value": 42.0,
      "unit": "l",
      "timestamp": "2026-10-23T18:36:45Z"
    }
  }
}
```
