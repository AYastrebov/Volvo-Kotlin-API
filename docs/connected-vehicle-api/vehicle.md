# Vehicle Endpoints

## List Vehicles

Get a list of vehicles associated with the authorized Volvo ID account.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles`

### Scopes Required
- `openid`
- `conve:vehicle_relation`

### Response

| Field | Description |
|-------|-------------|
| `vin` | 17-character Vehicle Identification Number |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": [
    { "vin": "YV1XZ12345F123456" },
    { "vin": "YV1AB98765G789012" }
  ]
}
```

---

## Get Vehicle Details

Get generic vehicle properties including model, year, color, and images.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}`

### Path Parameters
- `{vin}` - Vehicle Identification Number

### Scopes Required
- `openid`
- `conve:vehicle_relation`

### Response

| Field | Description |
|-------|-------------|
| `vin` | 17-character Vehicle Identification Number |
| `modelYear` | Vehicle's model year |
| `gearbox` | `AUTOMATIC` or `MANUAL` |
| `fuelType` | `DIESEL`, `PETROL`, `PETROL/ELECTRIC`, `DIESEL/ELECTRIC`, `ELECTRIC`, `NONE` |
| `externalColour` | Vehicle's exterior color |
| `batteryCapacityKWH` | Battery capacity in kWh (electric/hybrid only) |
| `images.exteriorImageUrl` | URL to exterior image (temporary, don't cache) |
| `images.interiorImageUrl` | URL to interior image (temporary, don't cache) |
| `descriptions.model` | Model name/code |
| `descriptions.steering` | Steering wheel description |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response**:
```json
{
  "data": {
    "vin": "YV1XZ12345F123456",
    "modelYear": 2024,
    "gearbox": "AUTOMATIC",
    "fuelType": "ELECTRIC",
    "externalColour": "BLACK",
    "batteryCapacityKWH": 78.0,
    "images": {
      "exteriorImageUrl": "https://example.com/exterior.jpg",
      "interiorImageUrl": "https://example.com/interior.jpg"
    },
    "descriptions": {
      "model": "XC40 Recharge",
      "steering": "LEFT"
    }
  }
}
```
