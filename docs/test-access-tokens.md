# Test Access Tokens

Test access tokens allow you to experiment with Volvo Cars APIs without going through the full OAuth2 authorization flow or publishing your application.

## Overview

The Developer Portal provides an Access Token Generation Tool that creates test tokens for development and experimentation purposes.

## Generating Test Tokens

1. Log in to the [Volvo Cars Developer Portal](https://developer.volvocars.com)
2. Navigate to your account settings
3. Use the Access Token Generation Tool
4. Select the API scopes you need
5. Generate a test token

## Using Test Tokens

Include the generated test token in your API requests:

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-test-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

## Demo Test Vehicle

When using test tokens, you can access a Demo Test Vehicle to experiment with API functionality without needing a real Volvo vehicle.

## Limitations

- Test tokens are for development and testing only
- Test tokens have limited validity periods
- For production applications, implement the full [OAuth2 authorization flow](authorization.md)

## Sandbox Environment

Each API provides a sandbox environment for testing:
- Connected Vehicle API
- Energy API
- Extended Vehicle API

You can test functionality using your own Volvo ID account or a demo Volvo ID account.
