# Fuel

## Get Fuel Amount

Get the vehicle's current fuel level.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/fuel`

### Scopes Required
- `openid`
- `conve:fuel_status`

### Response

| Field | Description |
|-------|-------------|
| `fuelAmount.value` | Current fuel amount |
| `fuelAmount.unit` | Unit of measurement (e.g., `liters`) |
| `fuelAmount.timestamp` | ISO-8601 timestamp of last retrieval |

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
    "fuelAmount": {
      "value": 45.5,
      "unit": "liters",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```
