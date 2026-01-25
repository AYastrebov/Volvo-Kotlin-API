# Climate

## Start Climatization

Start the vehicle's climate control system remotely.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/climatization-start`

### Scopes Required
- `openid`
- `conve:climatization_start_stop`

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
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/climatization-start' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

---

## Stop Climatization

Stop the vehicle's climate control system.

**POST** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/climatization-stop`

### Scopes Required
- `openid`
- `conve:climatization_start_stop`

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
curl -X POST 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/commands/climatization-stop' \
  -H 'content-type: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```
