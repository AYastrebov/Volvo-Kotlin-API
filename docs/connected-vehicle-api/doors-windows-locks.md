# Doors, Windows & Locks

## Get Window Status

Get the vehicle's latest window status values.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/windows`

### Scopes Required
- `openid`
- `conve:windows_status`

### Response

| Field | Description |
|-------|-------------|
| `frontLeftWindow.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `frontLeftWindow.timestamp` | ISO-8601 timestamp |
| `frontRightWindow.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `frontRightWindow.timestamp` | ISO-8601 timestamp |
| `rearLeftWindow.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `rearLeftWindow.timestamp` | ISO-8601 timestamp |
| `rearRightWindow.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `rearRightWindow.timestamp` | ISO-8601 timestamp |
| `sunroof.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` (if available) |
| `sunroof.timestamp` | ISO-8601 timestamp |

### Example

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/windows' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Get Door and Lock Status

Get door and lock status values.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/doors`

### Scopes Required
- `openid`
- `conve:doors_status`
- `conve:lock_status`

### Response

| Field | Description |
|-------|-------------|
| `centralLock.value` | `UNSPECIFIED`, `UNLOCKED`, `LOCKED` |
| `centralLock.timestamp` | ISO-8601 timestamp |
| `frontLeftDoor.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `frontRightDoor.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `rearLeftDoor.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `rearRightDoor.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `tailGate.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `hood.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |
| `tankLid.value` | `UNSPECIFIED`, `OPEN`, `CLOSED`, `AJAR` |

### Example

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/doors' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Lock Doors

Lock the vehicle's doors.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/lock`

### Scopes Required
- `openid`
- `conve:lock`

### Response

| Field | Description |
|-------|-------------|
| `vin` | Vehicle Identification Number |
| `invokeStatus` | Command status (see [Commands](commands.md)) |
| `message` | Detail message (if any) |

### Example

```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/lock' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Lock Doors with Reduced Guard

Lock the vehicle with reduced alarm sensitivity.

> **Note**: Not available for classic cars (without Android-based infotainment system).

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/lock-reduced-guard`

### Scopes Required
- `openid`
- `conve:lock`

### Example

```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/lock-reduced-guard' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Unlock Doors

Unlock the vehicle or trunk/boot doors. When the trunk is opened within the unlock duration, all doors will unlock.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/unlock`

### Scopes Required
- `openid`
- `conve:unlock`

### Response

| Field | Description |
|-------|-------------|
| `vin` | Vehicle Identification Number |
| `statusCode` | Status code |
| `invokeStatus` | Command status |
| `message` | Detail message (if any) |
| `readyToUnlock` | `true` or `false` |
| `readyToUnlockUntil` | Seconds left to unlock |

### Example

```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/unlock' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "vin": "YV1XZ12345F123456",
    "invokeStatus": "COMPLETED",
    "readyToUnlock": true,
    "readyToUnlockUntil": 60
  }
}
```
