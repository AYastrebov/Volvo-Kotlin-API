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
curl -X GET 'https://api.volvocars.com/energy/v2/vehicles/{vin}/state' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

## Status Codes

| Status Code | Description |
|-------------|-------------|
| `200 OK` | Successful request |
| `202 Accepted` | Request accepted, processing not completed |
| `204 No Content` | Successful request, no response body |
| `400 Bad Request` | Invalid request or JSON payload |
| `401 Unauthorized` | Invalid VCC-API-key or Bearer token |
| `404 Not Found` | Resource not found |
| `406 Not Acceptable` | Invalid Accept header mediatype |
| `409 Conflict` | Request conflict with current state |
| `422 Unprocessable Entity` | Passes validation but fails business rules |
| `500 Internal Server Error` | Server processing error |
| `502 Bad Gateway` | Upstream service error |

## Error Handling

Error responses include a message body:

```json
{
  "code": "INTERNAL_ERROR",
  "message": "An internal error occurred. Ref: c3140d113932b5f7c6549a48a647b52a",
  "details": []
}
```

### Error Fields

| Field | Description |
|-------|-------------|
| `code` | HTTP status code identifier |
| `message` | Error explanation |
| `details` | List of detailed error descriptions (may be empty) |

### Example Error with Details

```json
{
  "code": "REQUEST_VALIDATION_FAILED",
  "message": "Request validation failed, see details",
  "details": [
    {
      "code": "CHARGING_SCHEDULE_KIND_NOT_SUPPORTED",
      "message": "The provided charging schedule kind is not supported for this vehicle"
    }
  ]
}
```

## Debugging Errors

For troubleshooting, include the `vcc-api-operationId` header or note the internal error reference number.

```bash
-H 'vcc-api-operationId: <UUID>'
```

The operation ID will be returned in the response header. Contact developer.portal@volvocars.com for support.
