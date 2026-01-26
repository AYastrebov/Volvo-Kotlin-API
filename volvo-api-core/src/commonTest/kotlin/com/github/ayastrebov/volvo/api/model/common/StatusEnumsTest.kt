package com.github.ayastrebov.volvo.api.model.common

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for status enum parsing and type safety.
 */
class StatusEnumsTest {

    // ==================== InvokeStatus ====================

    @Test
    fun invokeStatus_parsesRunning() {
        assertEquals(InvokeStatus.RUNNING, InvokeStatus.fromString("RUNNING"))
        assertEquals(InvokeStatus.RUNNING, InvokeStatus.fromString("running"))
        assertEquals(InvokeStatus.RUNNING, InvokeStatus.fromString("Running"))
    }

    @Test
    fun invokeStatus_parsesCompleted() {
        assertEquals(InvokeStatus.COMPLETED, InvokeStatus.fromString("COMPLETED"))
    }

    @Test
    fun invokeStatus_parsesFailed() {
        assertEquals(InvokeStatus.FAILED, InvokeStatus.fromString("FAILED"))
    }

    @Test
    fun invokeStatus_unknownReturnsUnknown() {
        assertEquals(InvokeStatus.UNKNOWN, InvokeStatus.fromString("INVALID"))
        assertEquals(InvokeStatus.UNKNOWN, InvokeStatus.fromString(""))
        assertEquals(InvokeStatus.UNKNOWN, InvokeStatus.fromString(null))
    }

    // ==================== ChargerConnectionStatus ====================

    @Test
    fun chargerConnectionStatus_parsesConnected() {
        assertEquals(ChargerConnectionStatus.CONNECTED, ChargerConnectionStatus.fromString("CONNECTED"))
        assertEquals(ChargerConnectionStatus.CONNECTED, ChargerConnectionStatus.fromString("connected"))
    }

    @Test
    fun chargerConnectionStatus_parsesDisconnected() {
        assertEquals(ChargerConnectionStatus.DISCONNECTED, ChargerConnectionStatus.fromString("DISCONNECTED"))
    }

    @Test
    fun chargerConnectionStatus_parsesFault() {
        assertEquals(ChargerConnectionStatus.FAULT, ChargerConnectionStatus.fromString("FAULT"))
    }

    @Test
    fun chargerConnectionStatus_unknownReturnsUnknown() {
        assertEquals(ChargerConnectionStatus.UNKNOWN, ChargerConnectionStatus.fromString("INVALID"))
        assertEquals(ChargerConnectionStatus.UNKNOWN, ChargerConnectionStatus.fromString(null))
    }

    // ==================== ChargingStatus ====================

    @Test
    fun chargingStatus_parsesIdle() {
        assertEquals(ChargingStatus.IDLE, ChargingStatus.fromString("IDLE"))
    }

    @Test
    fun chargingStatus_parsesCharging() {
        assertEquals(ChargingStatus.CHARGING, ChargingStatus.fromString("CHARGING"))
    }

    @Test
    fun chargingStatus_parsesDone() {
        assertEquals(ChargingStatus.DONE, ChargingStatus.fromString("DONE"))
    }

    @Test
    fun chargingStatus_parsesFault() {
        assertEquals(ChargingStatus.FAULT, ChargingStatus.fromString("FAULT"))
    }

    @Test
    fun chargingStatus_parsesScheduled() {
        assertEquals(ChargingStatus.SCHEDULED, ChargingStatus.fromString("SCHEDULED"))
    }

    @Test
    fun chargingStatus_unknownReturnsUnknown() {
        assertEquals(ChargingStatus.UNKNOWN, ChargingStatus.fromString("INVALID"))
        assertEquals(ChargingStatus.UNKNOWN, ChargingStatus.fromString(null))
    }

    // ==================== ChargingType ====================

    @Test
    fun chargingType_parsesNone() {
        assertEquals(ChargingType.NONE, ChargingType.fromString("NONE"))
    }

    @Test
    fun chargingType_parsesAC() {
        assertEquals(ChargingType.AC, ChargingType.fromString("AC"))
    }

    @Test
    fun chargingType_parsesDC() {
        assertEquals(ChargingType.DC, ChargingType.fromString("DC"))
    }

    @Test
    fun chargingType_unknownReturnsUnknown() {
        assertEquals(ChargingType.UNKNOWN, ChargingType.fromString("INVALID"))
        assertEquals(ChargingType.UNKNOWN, ChargingType.fromString(null))
    }

    // ==================== ResourceStatus ====================

    @Test
    fun resourceStatus_parsesOK() {
        assertEquals(ResourceStatus.OK, ResourceStatus.fromString("OK"))
    }

    @Test
    fun resourceStatus_parsesError() {
        assertEquals(ResourceStatus.ERROR, ResourceStatus.fromString("ERROR"))
    }

    @Test
    fun resourceStatus_parsesNotAvailable() {
        assertEquals(ResourceStatus.NOT_AVAILABLE, ResourceStatus.fromString("NOT_AVAILABLE"))
    }

    @Test
    fun resourceStatus_unknownReturnsUnknown() {
        assertEquals(ResourceStatus.UNKNOWN, ResourceStatus.fromString("INVALID"))
        assertEquals(ResourceStatus.UNKNOWN, ResourceStatus.fromString(null))
    }

    // ==================== EngineRunningStatus ====================

    @Test
    fun engineRunningStatus_parsesStopped() {
        assertEquals(EngineRunningStatus.STOPPED, EngineRunningStatus.fromString("STOPPED"))
    }

    @Test
    fun engineRunningStatus_parsesRunning() {
        assertEquals(EngineRunningStatus.RUNNING, EngineRunningStatus.fromString("RUNNING"))
    }

    @Test
    fun engineRunningStatus_unknownReturnsUnknown() {
        assertEquals(EngineRunningStatus.UNKNOWN, EngineRunningStatus.fromString("INVALID"))
        assertEquals(EngineRunningStatus.UNKNOWN, EngineRunningStatus.fromString(null))
    }

    // ==================== OpenCloseStatus ====================

    @Test
    fun openCloseStatus_parsesOpen() {
        assertEquals(OpenCloseStatus.OPEN, OpenCloseStatus.fromString("OPEN"))
    }

    @Test
    fun openCloseStatus_parsesClosed() {
        assertEquals(OpenCloseStatus.CLOSED, OpenCloseStatus.fromString("CLOSED"))
    }

    @Test
    fun openCloseStatus_parsesAjar() {
        assertEquals(OpenCloseStatus.AJAR, OpenCloseStatus.fromString("AJAR"))
    }

    @Test
    fun openCloseStatus_unknownReturnsUnknown() {
        assertEquals(OpenCloseStatus.UNKNOWN, OpenCloseStatus.fromString("INVALID"))
        assertEquals(OpenCloseStatus.UNKNOWN, OpenCloseStatus.fromString(null))
    }

    // ==================== LockStatus ====================

    @Test
    fun lockStatus_parsesLocked() {
        assertEquals(LockStatus.LOCKED, LockStatus.fromString("LOCKED"))
    }

    @Test
    fun lockStatus_parsesUnlocked() {
        assertEquals(LockStatus.UNLOCKED, LockStatus.fromString("UNLOCKED"))
    }

    @Test
    fun lockStatus_unknownReturnsUnknown() {
        assertEquals(LockStatus.UNKNOWN, LockStatus.fromString("INVALID"))
        assertEquals(LockStatus.UNKNOWN, LockStatus.fromString(null))
    }

    // ==================== WarningStatus ====================

    @Test
    fun warningStatus_parsesNoWarning() {
        assertEquals(WarningStatus.NO_WARNING, WarningStatus.fromString("NO_WARNING"))
    }

    @Test
    fun warningStatus_parsesWarning() {
        assertEquals(WarningStatus.WARNING, WarningStatus.fromString("WARNING"))
    }

    @Test
    fun warningStatus_parsesServiceRequired() {
        assertEquals(WarningStatus.SERVICE_REQUIRED, WarningStatus.fromString("SERVICE_REQUIRED"))
    }

    @Test
    fun warningStatus_unknownReturnsUnknown() {
        assertEquals(WarningStatus.UNKNOWN, WarningStatus.fromString("INVALID"))
        assertEquals(WarningStatus.UNKNOWN, WarningStatus.fromString(null))
    }

    // ==================== Case Insensitivity ====================

    @Test
    fun allEnums_areCaseInsensitive() {
        assertEquals(InvokeStatus.RUNNING, InvokeStatus.fromString("running"))
        assertEquals(ChargerConnectionStatus.CONNECTED, ChargerConnectionStatus.fromString("connected"))
        assertEquals(ChargingStatus.CHARGING, ChargingStatus.fromString("charging"))
        assertEquals(ChargingType.AC, ChargingType.fromString("ac"))
        assertEquals(ResourceStatus.OK, ResourceStatus.fromString("ok"))
        assertEquals(EngineRunningStatus.RUNNING, EngineRunningStatus.fromString("running"))
        assertEquals(OpenCloseStatus.OPEN, OpenCloseStatus.fromString("open"))
        assertEquals(LockStatus.LOCKED, LockStatus.fromString("locked"))
        assertEquals(WarningStatus.WARNING, WarningStatus.fromString("warning"))
    }
}
