# Diagnostics

## Get Diagnostics

Get vehicle diagnostic information including service warnings and maintenance data.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/diagnostics`

### Scopes Required
- `openid`
- `conve:diagnostics`

### Response

| Field | Description |
|-------|-------------|
| `serviceWarning.value` | Service warning status (e.g., `NO_WARNING`) |
| `serviceWarning.timestamp` | ISO-8601 timestamp |
| `washerFluidLevelWarning.value` | Washer fluid level warning |
| `washerFluidLevelWarning.timestamp` | ISO-8601 timestamp |
| `distanceToService.value` | Distance until next service (km) |
| `distanceToService.unit` | Unit of measurement |
| `distanceToService.timestamp` | ISO-8601 timestamp |
| `timeToService.value` | Days until next service |
| `timeToService.unit` | Unit of measurement |
| `timeToService.timestamp` | ISO-8601 timestamp |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/diagnostics' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "serviceWarning": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "washerFluidLevelWarning": {
      "value": "NO_WARNING",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "distanceToService": {
      "value": 15000,
      "unit": "km",
      "timestamp": "2024-01-15T10:30:00Z"
    },
    "timeToService": {
      "value": 180,
      "unit": "days",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```

---

## Get Brake Status

Get brake fluid level warning status.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/brakes`

### Scopes Required
- `openid`
- `conve:brake_status`

### Response

| Field | Description |
|-------|-------------|
| `brakeFluidLevelWarning.value` | `NO_WARNING` or warning status |
| `brakeFluidLevelWarning.timestamp` | ISO-8601 timestamp |

### Example

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/brakes' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Get Engine Diagnostics

Get engine diagnostic values including coolant and oil level warnings.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/engine`

### Scopes Required
- `openid`
- `conve:engine_diagnostics`

### Response

| Field | Description |
|-------|-------------|
| `engineCoolantLevelWarning.value` | Coolant level warning status |
| `engineCoolantLevelWarning.timestamp` | ISO-8601 timestamp |
| `oilLevelWarning.value` | Oil level warning status |
| `oilLevelWarning.timestamp` | ISO-8601 timestamp |

### Example

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/engine' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```
