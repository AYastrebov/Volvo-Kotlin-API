# Statistics

## Get Statistics

Get vehicle statistics including fuel consumption, speed, and trip data.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/statistics`

### Scopes Required
- `openid`
- `conve:statistics`

### Response

| Field | Description |
|-------|-------------|
| `averageFuelConsumption.value` | Average fuel consumption |
| `averageFuelConsumption.unit` | Unit of measurement |
| `averageFuelConsumption.timestamp` | ISO-8601 timestamp |
| `averageSpeed.value` | Average speed |
| `averageSpeed.unit` | Unit of measurement |
| `averageSpeed.timestamp` | ISO-8601 timestamp |
| `tripMeter1.value` | Trip meter 1 reading |
| `tripMeter1.unit` | Unit of measurement |
| `tripMeter1.timestamp` | ISO-8601 timestamp |
| `tripMeter2.value` | Trip meter 2 reading |
| `tripMeter2.unit` | Unit of measurement |
| `tripMeter2.timestamp` | ISO-8601 timestamp |
| `distanceToEmpty.value` | Estimated distance to empty tank |
| `distanceToEmpty.unit` | Unit of measurement |
| `distanceToEmpty.timestamp` | ISO-8601 timestamp |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/statistics' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "averageFuelConsumption": {
      "value": "5.2",
      "unit": "l/100km",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "averageSpeed": {
      "value": "45",
      "unit": "km/h",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "tripMeter1": {
      "value": "1250",
      "unit": "km",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "tripMeter2": {
      "value": "560",
      "unit": "km",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "distanceToEmpty": {
      "value": "450",
      "unit": "km",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```
