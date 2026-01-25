# Tyres

## Get Tyre Status

Get tyre pressure warning status for all wheels.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/tyres`

### Scopes Required
- `openid`
- `conve:tyre_status`

### Response

| Field | Description |
|-------|-------------|
| `frontLeft.value` | `NO_WARNING` or warning status |
| `frontLeft.timestamp` | ISO-8601 timestamp |
| `frontRight.value` | `NO_WARNING` or warning status |
| `frontRight.timestamp` | ISO-8601 timestamp |
| `rearLeft.value` | `NO_WARNING` or warning status |
| `rearLeft.timestamp` | ISO-8601 timestamp |
| `rearRight.value` | `NO_WARNING` or warning status |
| `rearRight.timestamp` | ISO-8601 timestamp |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/tyres' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "frontLeft": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "frontRight": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "rearLeft": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "rearRight": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```
