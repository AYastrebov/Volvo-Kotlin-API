# API Details

## Application Setup

To use this API, create an application in the [Developer Portal](https://developer.volvocars.com). You'll receive a VCC API key to include in all requests.

## Required Headers

All requests require these headers:

| Header | Description |
|--------|-------------|
| `accept` | `application/json` |
| `authorization` | `Bearer <your-access-token>` |
| `vcc-api-key` | `<your-vcc-api-key>` |

**Example**:
```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

## Response Format

Successful responses (except 204) contain a `data` field with endpoint-specific information:

```json
{
  "data": {
    "brakeFluidLevelWarning": {
      "value": "NO_WARNING",
      "timestamp": "2023-11-14T12:16:34.324Z"
    }
  }
}
```

## Status Codes

| Status Code | Description |
|-------------|-------------|
| `200 OK` | Successful request |
| `202 Accepted` | Request accepted, processing not completed |
| `204 No Content` | Successful request, no response body |
| `400 Bad Request` | Invalid request or JSON payload |
| `401 Unauthorized` | Invalid VCC-API-key or Bearer token |
| `403 Resource Forbidden` | Missing scopes in Bearer token |
| `404 Not Found` | Resource not found |
| `406 Not Acceptable` | Invalid Accept header mediatype |
| `415 Unsupported Media Type` | Invalid Content-Type header |
| `422 Unprocessable Entity` | Passes validation but fails business rules |
| `500 Internal Server Error` | Server processing error |
| `502 Bad Gateway` | Upstream service error |
| `503 Service Unavailable` | Server temporarily unavailable |
| `504 Gateway Timeout` | Upstream timeout |

## Error Handling

Error responses include a message body:

```json
{
  "error": {
    "message": "SERVICE_UNAVAILABLE",
    "description": "Not allowed, car is in use or asleep"
  }
}
```

| Field | Description |
|-------|-------------|
| `error.message` | Error explanation |
| `error.description` | Detailed error description |
| `error.detail` | Additional details (format varies by endpoint) |

## Debugging Errors

For troubleshooting, include a `traceparent` header in your request:

```bash
-H 'traceparent: 00-<trace id>-<parent id>-<trace flags>'
```

See [W3C Trace Context](https://www.w3.org/TR/trace-context/) for header format. Contact developer.portal@volvocars.com for support.

## Vehicle Sleep Mode

When a vehicle hasn't been used for 3-5 days, it enters standby mode and may not respond to invocation commands. Commands work again when the vehicle is started.

### Command Error Statuses

| Status | Description |
|--------|-------------|
| `VEHICLE_REQUEST_TIMEOUT` | Timeout sending request to vehicle (poor connectivity) |
| `VEHICLE_RESPONSE_TIMEOUT` | Timeout waiting for vehicle response |
| `VEHICLE_RESPONSE_ERROR` | Vehicle error |
| `VEHICLE_IN_SLEEP_MODE` | Vehicle in sleep mode |
| `UNLOCK_TIME_FRAME_PASSED` | User didn't unlock within time limit |
| `UNABLE_TO_LOCK_DOOR_OPEN` | Cannot lock due to open door |
| `PRIVACY_POLICY_ENABLED` | Privacy policy enabled on vehicle |

**Example Response** (vehicle in sleep mode):
```json
{
  "data": {
    "created": "2021-05-03T10:19:16.762Z",
    "updated": "2021-05-03T10:19:18.133Z",
    "command": "FLASH",
    "requestId": "6567aa84-0d96-485e-b140-2be516d430db",
    "invokeStatus": "VEHICLE_IN_SLEEP"
  }
}
```
