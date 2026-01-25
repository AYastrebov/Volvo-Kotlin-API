# Authorization

Volvo Cars APIs implement the [OAuth 2.0 authorization framework](https://oauth.net/2/) for secure access to user data.

## Overview

End-users delegate access to their data to your application through their Volvo ID account. To start the authorization process:

1. **Publish your application** on your account page to receive client credentials
2. The client credentials consist of a `client_id` and `client_secret`

> **Note**: For testing without publishing, use [test access tokens](test-access-tokens.md).

> **Important**: All new published applications enforce PKCE (Proof Key for Code Exchange).

## Authorization Code Flow

### Step 1: Redirect to Volvo ID Login

Redirect the end-user to the Volvo ID login page. After authentication, they'll be asked to consent to the requested scopes.

**URL**: `https://volvoid.eu.volvocars.com/as/authorization.oauth2`

**Query Parameters**:

| Parameter | Description |
|-----------|-------------|
| `response_type` | Set to `code` |
| `client_id` | ID of your client credentials |
| `redirect_uri` | Valid callback URI registered when publishing (URL encoded) |
| `scope` | Scopes that the user needs to consent to (space-separated, URL encoded) |
| `state` | Opaque value for maintaining state between request and callback (highly recommended for CSRF prevention) |

**Example URL**:
```
https://volvoid.eu.volvocars.com/as/authorization.oauth2?response_type=code&client_id=<your-client-id>&redirect_uri=<your-redirect-uri>&scope=<api-scopes>&state=<your-client-state>
```

**Success Response** (redirected to your callback URI):

| Parameter | Description |
|-----------|-------------|
| `code` | Authorization code to exchange for access token |
| `state` | Same value as sent in request (if provided) |

**Error Response**:

| Parameter | Description |
|-----------|-------------|
| `error` | Error code |
| `error_description` | Detailed error description |
| `state` | Same value as sent in request (if provided) |

### Step 2: Exchange Code for Access Token

Exchange the authorization code for an access token.

**POST** `https://volvoid.eu.volvocars.com/as/token.oauth2`

**Headers**:

| Header | Description |
|--------|-------------|
| `content-type` | `application/x-www-form-urlencoded` |
| `authorization` | `Basic <base64-encoded client_id:client_secret>` |

**Request Body**:

| Parameter | Description |
|-----------|-------------|
| `grant_type` | Set to `authorization_code` |
| `code` | Authorization code from Step 1 |
| `redirect_uri` | Same callback URI used in Step 1 (URL encoded) |

**Example**:
```bash
curl POST 'https://volvoid.eu.volvocars.com/as/token.oauth2' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'authorization: Basic <base64-encoded-client-credentials>' \
  -d 'grant_type=authorization_code&code=<authorisation-code>&redirect_uri=<redirect-uri>'
```

**Response**:

| Field | Description |
|-------|-------------|
| `access_token` | Bearer token for API calls |
| `refresh_token` | Token to obtain new access tokens |
| `token_type` | Always `Bearer` |
| `expires_in` | Seconds until access_token expires |

### Step 3: Refresh the Access Token

Access tokens have short lifespans. Refresh before expiration for seamless user experience.

**POST** `https://volvoid.eu.volvocars.com/as/token.oauth2`

**Headers**:

| Header | Description |
|--------|-------------|
| `content-type` | `application/x-www-form-urlencoded` |
| `authorization` | `Basic <base64-encoded client_id:client_secret>` |

**Request Body**:

| Parameter | Description |
|-----------|-------------|
| `grant_type` | Set to `refresh_token` |
| `refresh_token` | Refresh token from authorization |

**Example**:
```bash
curl POST 'https://volvoid.eu.volvocars.com/as/token.oauth2' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'authorization: Basic <base64-encoded-client-credentials>' \
  -d 'grant_type=refresh_token&refresh_token=<refresh-token>'
```

**Important Notes**:
- Refresh token rotation is enabled by default (new refresh token with each use, old token invalidated)
- Refresh token must be used within **7 days** or it's invalidated
- Maximum grant lifetime is **6 months** with continuous refreshing

## Proof Key for Code Exchange (PKCE)

All new applications enforce [PKCE](https://oauth.net/2/pkce/) for security against authorization code interception attacks.

### Additional Authorization Parameters

| Parameter | Description |
|-----------|-------------|
| `code_challenge` | Challenge derived from `code_verifier` and `code_challenge_method` |
| `code_challenge_method` | Method to derive the challenge (e.g., `S256`) |

### Additional Token Parameters

| Parameter | Description |
|-----------|-------------|
| `code_verifier` | Cryptographically random string used to derive the code_challenge |

## Scopes

Each API endpoint requires specific scopes for end-user consent. Scopes needed for an endpoint are specified in that endpoint's documentation.

### Common Scopes

| Scope | Description |
|-------|-------------|
| `openid` | Required for all requests |
| `conve:vehicle_relation` | Access to vehicle list |
| `conve:commands` | List available commands |
| `conve:lock` | Lock/unlock commands |
| `conve:engine_status` | Engine status data |
| `conve:doors_status` | Door status data |
| `conve:windows_status` | Window status data |
| `energy:capability:read` | Energy capabilities |
| `energy:state:read` | Energy state data |

See individual API documentation for complete scope lists.

## Sample Implementation

For a sample OAuth2 implementation, see the [OAuth2 Code Flow sample on GitHub](https://github.com/volvo-cars/sample).
