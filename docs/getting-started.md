# Getting Started

This guide will help you get started with Volvo Cars' APIs using the Connected Vehicle API and curl commands.

## Prerequisites

### 1. Create an Account

Sign up at the [Volvo Cars Developer Portal](https://developer.volvocars.com) using Google or GitHub to create an account.

> **Note**: Volvo Cars employees should sign up using CDSID for access to additional APIs and tools.

### 2. Create an Application

To use the APIs, connect your Developer Portal account to an application. When created, you'll receive a **VCC API key** that must be included in the header of each request.

### 3. Generate Access Tokens

To access Volvo Cars user data and features, you need end-user permission through their Volvo ID account. Once granted, you'll receive a temporary access token.

For testing, use the [Access Token Generation Tool](https://developer.volvocars.com/account/access-tokens) to generate tokens for the Connected Vehicle API.

## Your First API Request

### 1. Get a VIN Number

A Volvo ID account can contain several registered vehicles. Get a list using the vehicle list endpoint:

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

The response will contain a list of vehicles, each with a unique VIN.

### 2. Get Current Engine Status

Using a VIN from the previous step, check the engine status:

```bash
curl -X GET 'https://api.volvocars.com/connected-vehicle/v2/vehicles/{vin}/engine-status' \
  -H 'accept: application/json' \
  -H 'authorization: Bearer <your-access-token>' \
  -H 'vcc-api-key: <your-vcc-api-key>'
```

> **Tip**: Most value responses include a timestamp indicating when the value was last collected from the vehicle.

## Next Steps

- Explore [sample projects on GitHub](https://github.com/volvo-cars/sample)
- View the full [Connected Vehicle API documentation](connected-vehicle-api/README.md)
- Browse the [Energy API](energy-api/README.md) for electric vehicle data
- Read about [authorization](authorization.md) for production deployment

## Publishing Your Application

Once your application is ready for production, you'll need to publish it before it can be made available to end-users. See the [Authorization](authorization.md) page for details on the publishing process.
