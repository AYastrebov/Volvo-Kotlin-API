# Commands

## List Commands

Get available commands for a specific vehicle.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands`

### Scopes Required
- `openid`
- `conve:commands`

### Response

| Field | Description |
|-------|-------------|
| `command` | Name of the available command |
| `href` | URL for the specific command |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": [
    {
      "command": "LOCK",
      "href": "https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/lock"
    },
    {
      "command": "UNLOCK",
      "href": "https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/unlock"
    },
    {
      "command": "FLASH",
      "href": "https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/flash"
    },
    {
      "command": "HONK",
      "href": "https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/honk"
    }
  ]
}
```

---

## Get Command Accessibility

Check if a vehicle is accessible for receiving invocation commands. If unavailable, the reason is provided.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/command-accessibility`

### Scopes Required
- `openid`
- `conve:command_accessibility`

### Response

| Field | Description |
|-------|-------------|
| `availabilityStatus.value` | `UNSPECIFIED`, `AVAILABLE`, `UNAVAILABLE` |
| `availabilityStatus.unavailableReason` | `UNSPECIFIED`, `NO_INTERNET`, `POWER_SAVING_MODE`, `CAR_IN_USE` |
| `availabilityStatus.timestamp` | ISO-8601 timestamp of last retrieval |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/command-accessibility' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "availabilityStatus": {
      "value": "AVAILABLE",
      "timestamp": "2024-01-15T10:30:00Z"
    }
  }
}
```

---

## Command Invocation Status Values

When executing commands, the `invokeStatus` field indicates the result:

| Status | Description |
|--------|-------------|
| `COMPLETED` | Command executed successfully |
| `DELIVERED` | Command delivered to vehicle |
| `RUNNING` | Command in progress |
| `WAITING` | Command queued |
| `TIMEOUT` | Request timed out |
| `CONNECTION_FAILURE` | Failed to connect to vehicle |
| `VEHICLE_IN_SLEEP` | Vehicle in sleep mode |
| `UNABLE_TO_LOCK_DOOR_OPEN` | Cannot lock - door is open |
| `UNLOCK_TIME_FRAME_PASSED` | Unlock time limit expired |
| `REJECTED` | Command rejected by vehicle |
| `NOT_ALLOWED_PRIVACY_ENABLED` | Privacy mode enabled |
| `NOT_ALLOWED_WRONG_USAGE_MODE` | Invalid usage mode |
| `CAR_ERROR` | Vehicle error |
| `UNKNOWN` | Unknown status |
