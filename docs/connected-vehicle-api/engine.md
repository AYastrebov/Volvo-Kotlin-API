# Engine

## Get Engine Status

Get the vehicle's latest engine status.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/engine-status`

### Scopes Required
- `openid`
- `conve:engine_status`

### Response

| Field | Description |
|-------|-------------|
| `engineStatus.value` | `STOPPED` or `RUNNING` |
| `engineStatus.timestamp` | ISO-8601 timestamp of last retrieval |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/engine-status' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "engineStatus": {
      "value": "STOPPED",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```

---

## Start Engine

Start the vehicle's engine remotely.

> **Note**: Use [List commands](commands.md) to check if this function is supported for a specific vehicle.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/engine-start`

### Scopes Required
- `openid`
- `conve:engine_start_stop`

### Request Body

| Field | Description |
|-------|-------------|
| `runtimeMinutes` | **Required**. Duration in minutes (max 15) |

### Response

| Field | Description |
|-------|-------------|
| `vin` | Vehicle Identification Number |
| `statusCode` | Response status code |
| `invokeStatus` | `RUNNING`, `WAITING`, `COMPLETED`, `REJECTED`, `UNKNOWN`, `TIMEOUT`, `CONNECTION_FAILURE`, `VEHICLE_IN_SLEEP`, `DELIVERED`, `CAR_ERROR`, `NOT_ALLOWED_PRIVACY_ENABLED`, `NOT_ALLOWED_WRONG_USAGE_MODE` |
| `message` | Extra information |

### Example

**Request**:
```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/engine-start' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>' \
  -d '{"runtimeMinutes": 10}'
```

**Response**:
```json
{
  "data": {
    "vin": "YV1XZ12345F123456",
    "invokeStatus": "COMPLETED",
    "message": ""
  }
}
```

---

## Stop Engine

Stop the vehicle's engine remotely.

> **Note**: Use [List commands](commands.md) to check if this function is supported for a specific vehicle.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/engine-stop`

### Scopes Required
- `openid`
- `conve:engine_start_stop`

### Request Body

No request body required.

### Response

| Field | Description |
|-------|-------------|
| `vin` | Vehicle Identification Number |
| `statusCode` | Response status code |
| `invokeStatus` | Command status |
| `message` | Extra information |

### Example

```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/engine-stop' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```
