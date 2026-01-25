# Lights & Sound

## Flash Lights

Flash the vehicle's lights to help locate it.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/flash`

### Scopes Required
- `openid`
- `conve:honk_flash`

### Request Body

No request body required.

### Response

| Field | Description |
|-------|-------------|
| `vin` | Vehicle Identification Number |
| `invokeStatus` | Command status |
| `message` | Detail message (if any) |

### Example

```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/flash' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Sound Horn

Sound the vehicle's horn.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/honk`

### Scopes Required
- `openid`
- `conve:honk_flash`

### Request Body

No request body required.

### Response

| Field | Description |
|-------|-------------|
| `vin` | Vehicle Identification Number |
| `invokeStatus` | Command status |
| `message` | Detail message (if any) |

### Example

```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/honk' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Honk and Flash

Sound the horn and flash lights simultaneously.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/honk-flash`

### Scopes Required
- `openid`
- `conve:honk_flash`

### Request Body

No request body required.

### Response

| Field | Description |
|-------|-------------|
| `vin` | Vehicle Identification Number |
| `invokeStatus` | Command status |
| `message` | Detail message (if any) |

### Example

```bash
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/honk-flash' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```
