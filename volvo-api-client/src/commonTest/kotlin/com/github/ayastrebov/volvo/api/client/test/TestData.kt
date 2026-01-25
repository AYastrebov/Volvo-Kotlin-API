package com.github.ayastrebov.volvo.api.client.test

/**
 * Test data constants and fixtures for API testing.
 */
object TestData {
    const val TEST_VIN = "YV1XZ00ABC1234567"
    const val TEST_API_KEY = "test-api-key"
    const val TEST_TOKEN = "test-access-token"
    const val TEST_OPERATION_ID = "test-operation-123"
}

/**
 * JSON fixtures for Connected Vehicle API responses.
 */
object ConnectedVehicleFixtures {

    val vehicleListResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": [
                {"vin": "${TestData.TEST_VIN}"},
                {"vin": "YV1XZ00DEF7654321"}
            ]
        }
    """.trimIndent()

    val vehicleDetailsResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "vin": "${TestData.TEST_VIN}",
                "modelYear": 2024,
                "gearbox": "AUTOMATIC",
                "fuelType": "ELECTRIC",
                "externalColour": "BLACK",
                "batteryCapacityKWH": 78.0,
                "images": {
                    "exteriorImageUrl": "https://example.com/exterior.jpg",
                    "internalImageUrl": "https://example.com/interior.jpg"
                },
                "descriptions": {
                    "model": "XC40 Recharge",
                    "upholstery": "Leather",
                    "steering": "LEFT_HAND_DRIVE"
                }
            }
        }
    """.trimIndent()

    val windowStatusResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "frontLeftWindow": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "frontRightWindow": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "rearLeftWindow": {"value": "OPEN", "timestamp": "2024-01-15T10:30:00Z"},
                "rearRightWindow": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "sunroof": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"}
            }
        }
    """.trimIndent()

    val warningsResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "brakeLightCenterWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "brakeLightLeftWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "brakeLightRightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "fogLightFrontWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "fogLightRearWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "hazardLightsWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "highBeamLeftWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "highBeamRightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "lowBeamLeftWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "lowBeamRightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "positionLightFrontLeftWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "positionLightFrontRightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "positionLightRearLeftWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "positionLightRearRightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "registrationPlateLightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "reverseLightsWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "sideMarkLightsWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "turnIndicationFrontLeftWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "turnIndicationFrontRightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "turnIndicationRearLeftWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "turnIndicationRearRightWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"}
            }
        }
    """.trimIndent()

    val tyreStatusResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "frontLeft": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "frontRight": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "rearLeft": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "rearRight": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"}
            }
        }
    """.trimIndent()

    val statisticsResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "averageFuelConsumption": {"value": "5.2", "timestamp": "2024-01-15T10:30:00Z", "unit": "l/100km"},
                "averageSpeed": {"value": "45", "timestamp": "2024-01-15T10:30:00Z", "unit": "km/h"},
                "tripMeter1": {"value": "1250", "timestamp": "2024-01-15T10:30:00Z", "unit": "km"},
                "tripMeter2": {"value": "560", "timestamp": "2024-01-15T10:30:00Z", "unit": "km"},
                "distanceToEmpty": {"value": "450", "timestamp": "2024-01-15T10:30:00Z", "unit": "km"}
            }
        }
    """.trimIndent()

    val odometerResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "odometer": {"value": 52340, "timestamp": "2024-01-15T10:30:00Z", "unit": "km"}
            }
        }
    """.trimIndent()

    val fuelAmountResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "fuelAmount": {"value": 45.5, "timestamp": "2024-01-15T10:30:00Z", "unit": "liters"}
            }
        }
    """.trimIndent()

    val engineDiagnosticsResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "engineCoolantLevelWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "oilLevelWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"}
            }
        }
    """.trimIndent()

    val engineStatusResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "engineStatus": {"value": "STOPPED", "timestamp": "2024-01-15T10:30:00Z"}
            }
        }
    """.trimIndent()

    val doorAndLockStatusResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "centralLock": {"value": "LOCKED", "timestamp": "2024-01-15T10:30:00Z"},
                "frontLeftDoor": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "frontRightDoor": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "rearLeftDoor": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "rearRightDoor": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "hood": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "tailgate": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"},
                "tankLid": {"value": "CLOSED", "timestamp": "2024-01-15T10:30:00Z"}
            }
        }
    """.trimIndent()

    val diagnosticsResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "serviceWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "washerFluidLevelWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"},
                "distanceToService": {"value": 15000, "timestamp": "2024-01-15T10:30:00Z", "unit": "km"},
                "timeToService": {"value": 180, "timestamp": "2024-01-15T10:30:00Z", "unit": "days"}
            }
        }
    """.trimIndent()

    val brakeStatusResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "brakeFluidLevelWarning": {"value": "NO_WARNING", "timestamp": "2024-01-15T10:30:00Z"}
            }
        }
    """.trimIndent()

    val commandListResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": [
                {"command": "LOCK", "href": "/vehicles/${TestData.TEST_VIN}/commands/lock"},
                {"command": "UNLOCK", "href": "/vehicles/${TestData.TEST_VIN}/commands/unlock"},
                {"command": "HONK", "href": "/vehicles/${TestData.TEST_VIN}/commands/honk"},
                {"command": "FLASH", "href": "/vehicles/${TestData.TEST_VIN}/commands/flash"},
                {"command": "HONK_FLASH", "href": "/vehicles/${TestData.TEST_VIN}/commands/honk-flash"},
                {"command": "CLIMATIZATION_START", "href": "/vehicles/${TestData.TEST_VIN}/commands/climatization-start"},
                {"command": "CLIMATIZATION_STOP", "href": "/vehicles/${TestData.TEST_VIN}/commands/climatization-stop"}
            ]
        }
    """.trimIndent()

    val commandAccessibilityResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "availableCommands": [
                    {"command": "LOCK", "href": "/vehicles/${TestData.TEST_VIN}/commands/lock"},
                    {"command": "UNLOCK", "href": "/vehicles/${TestData.TEST_VIN}/commands/unlock"}
                ],
                "unavailableCommands": [
                    {"command": "ENGINE_START", "reason": "NOT_SUPPORTED"}
                ]
            }
        }
    """.trimIndent()

    val commandResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "vin": "${TestData.TEST_VIN}",
                "invokeStatus": "COMPLETED",
                "message": "Command executed successfully"
            }
        }
    """.trimIndent()

    val unlockCommandResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "vin": "${TestData.TEST_VIN}",
                "invokeStatus": "COMPLETED",
                "message": "Unlock command executed successfully",
                "readyToUnlock": true,
                "readyToUnlockUntil": 1705315800000
            }
        }
    """.trimIndent()
}

/**
 * JSON fixtures for Energy API responses.
 */
object EnergyFixtures {

    val capabilitiesResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "getEnergyState": {
                    "isSupported": true,
                    "batteryChargeLevel": {"isSupported": true},
                    "electricRange": {"isSupported": true},
                    "chargerConnectionStatus": {"isSupported": true},
                    "chargingSystemStatus": {"isSupported": true},
                    "chargingType": {"isSupported": true},
                    "chargerPowerStatus": {"isSupported": true},
                    "estimatedChargingTimeToTargetBatteryChargeLevel": {"isSupported": true},
                    "chargingCurrentLimit": {"isSupported": true},
                    "targetBatteryChargeLevel": {"isSupported": true},
                    "chargingPower": {"isSupported": false}
                }
            }
        }
    """.trimIndent()

    val energyStateResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "batteryChargeLevel": {
                    "status": "OK",
                    "value": 78.5,
                    "updatedAt": "2024-01-15T10:30:00Z",
                    "unit": "percent"
                },
                "electricRange": {
                    "status": "OK",
                    "value": 285,
                    "updatedAt": "2024-01-15T10:30:00Z",
                    "unit": "km"
                },
                "chargerConnectionStatus": {
                    "status": "OK",
                    "value": "CONNECTED",
                    "updatedAt": "2024-01-15T10:30:00Z"
                },
                "chargingStatus": {
                    "status": "OK",
                    "value": "CHARGING",
                    "updatedAt": "2024-01-15T10:30:00Z"
                },
                "chargingType": {
                    "status": "OK",
                    "value": "AC",
                    "updatedAt": "2024-01-15T10:30:00Z"
                },
                "chargerPowerStatus": {
                    "status": "OK",
                    "value": "HIGH_POWER",
                    "updatedAt": "2024-01-15T10:30:00Z"
                },
                "estimatedChargingTimeToTargetBatteryChargeLevel": {
                    "status": "OK",
                    "value": 120,
                    "updatedAt": "2024-01-15T10:30:00Z",
                    "unit": "minutes"
                },
                "targetBatteryChargeLevel": {
                    "status": "OK",
                    "value": 80,
                    "updatedAt": "2024-01-15T10:30:00Z",
                    "unit": "percent"
                },
                "chargingCurrentLimit": {
                    "status": "OK",
                    "value": 16,
                    "updatedAt": "2024-01-15T10:30:00Z",
                    "unit": "ampere"
                }
            }
        }
    """.trimIndent()

    val energyStateErrorResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "batteryChargeLevel": {
                    "status": "ERROR",
                    "code": "DATA_NOT_AVAILABLE",
                    "message": "Data is temporarily unavailable"
                },
                "electricRange": {
                    "status": "OK",
                    "value": 285,
                    "updatedAt": "2024-01-15T10:30:00Z",
                    "unit": "km"
                }
            }
        }
    """.trimIndent()
}

/**
 * JSON fixtures for Location API responses.
 */
object LocationFixtures {

    val locationResponse = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "type": "Feature",
                "properties": {
                    "heading": "125"
                },
                "geometry": {
                    "type": "Point",
                    "coordinates": [18.0686, 59.3293, 25.5]
                }
            }
        }
    """.trimIndent()

    val locationResponseWithoutAltitude = """
        {
            "status": 200,
            "operationId": "${TestData.TEST_OPERATION_ID}",
            "data": {
                "type": "Feature",
                "properties": {},
                "geometry": {
                    "type": "Point",
                    "coordinates": [18.0686, 59.3293]
                }
            }
        }
    """.trimIndent()
}

/**
 * JSON fixtures for error responses.
 */
object ErrorFixtures {

    val error400 = """
        {
            "error": {
                "code": "INVALID_REQUEST",
                "message": "The request body is invalid or malformed"
            }
        }
    """.trimIndent()

    val error401 = """
        {
            "error": {
                "code": "UNAUTHORIZED",
                "message": "Invalid or expired access token"
            }
        }
    """.trimIndent()

    val error403 = """
        {
            "error": {
                "code": "FORBIDDEN",
                "message": "You don't have permission to access this resource"
            }
        }
    """.trimIndent()

    val error404 = """
        {
            "error": {
                "code": "NOT_FOUND",
                "message": "Vehicle not found"
            }
        }
    """.trimIndent()

    val error429 = """
        {
            "error": {
                "code": "RATE_LIMIT_EXCEEDED",
                "message": "Too many requests. Please try again later"
            }
        }
    """.trimIndent()

    val error500 = """
        {
            "error": {
                "code": "INTERNAL_SERVER_ERROR",
                "message": "An unexpected error occurred"
            }
        }
    """.trimIndent()
}
