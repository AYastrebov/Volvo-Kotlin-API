# Odometer

## Get Odometer

Get the vehicle's current odometer reading.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/odometer`

### Scopes Required
- `openid`
- `conve:odometer_status`

### Response

| Field | Description |
|-------|-------------|
| `odometer.value` | Current odometer reading |
| `odometer.unit` | Unit of measurement (e.g., `km`) |
| `odometer.timestamp` | ISO-8601 timestamp of last retrieval |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/odometer' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "odometer": {
      "value": 52340,
      "unit": "km",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```
