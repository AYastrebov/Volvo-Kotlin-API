# Statistics

## Get Statistics

Get vehicle trip statistics: consumption, speed, trip meters and estimated range.

**GET** `https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/statistics`

### Scopes Required
- `openid`
- `conve:trip_statistics`

### Response

Every field is a `{value, unit, timestamp}` object. Values are numeric on the wire; the published
OpenAPI specification still describes them as strings and lists only the five fields marked
*(specification)*, which current vehicles do not return. The client model (`Statistics`) carries both sets.

| Field | Description |
|-------|-------------|
| `averageSpeed` | Average speed, km/h |
| `averageSpeedAutomatic` | Average speed since the automatic trip meter was reset, km/h |
| `averageFuelConsumption` | Average fuel consumption, l/100km |
| `averageFuelConsumptionAutomatic` | Average fuel consumption since the automatic trip meter was reset, l/100km |
| `averageEnergyConsumption` | Average electric energy consumption, kWh/100km |
| `averageEnergyConsumptionAutomatic` | Average energy consumption since the automatic trip meter was reset, kWh/100km |
| `averageEnergyConsumptionSinceCharge` | Average energy consumption since the last charge, kWh/100km |
| `distanceToEmptyTank` | Estimated range on remaining fuel, km |
| `distanceToEmptyBattery` | Estimated range on remaining battery charge, km |
| `tripMeterAutomatic` | Automatic trip meter, km |
| `tripMeterManual` | Manual trip meter, km |
| `distanceToEmpty` | Estimated distance to empty, km *(specification)* |
| `tripMeter1` | Trip meter 1, km *(specification)* |
| `tripMeter2` | Trip meter 2, km *(specification)* |

### Example

**Request**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/statistics' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

**Response** (as returned by the live API for a demo vehicle):
```json
{
  "data": {
    "averageEnergyConsumption": { "value": 2.2374, "unit": "kWh/100km", "timestamp": "2026-10-23T18:36:45Z" },
    "averageEnergyConsumptionAutomatic": { "value": 2.4732, "unit": "kWh/100km", "timestamp": "2026-10-23T18:36:45Z" },
    "averageEnergyConsumptionSinceCharge": { "value": 2.4732, "unit": "kWh/100km", "timestamp": "2026-10-23T18:36:45Z" },
    "averageFuelConsumption": { "value": 7.2374, "unit": "l/100km", "timestamp": "2026-10-23T18:36:45Z" },
    "averageFuelConsumptionAutomatic": { "value": 7.4732, "unit": "l/100km", "timestamp": "2026-10-23T18:36:45Z" },
    "averageSpeed": { "value": 50, "unit": "km/h", "timestamp": "2026-10-23T18:36:45Z" },
    "averageSpeedAutomatic": { "value": 66, "unit": "km/h", "timestamp": "2026-10-23T18:36:45Z" },
    "distanceToEmptyBattery": { "value": 200, "unit": "km", "timestamp": "2026-10-23T18:36:45Z" },
    "distanceToEmptyTank": { "value": 1312, "unit": "km", "timestamp": "2026-10-23T18:36:45Z" },
    "tripMeterAutomatic": { "value": 420.0, "unit": "km", "timestamp": "2026-10-23T18:36:45Z" },
    "tripMeterManual": { "value": 500.0, "unit": "km", "timestamp": "2026-10-23T18:36:45Z" }
  }
}
```
