package com.github.ayastrebov.volvo.api.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Status of a command invocation.
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class InvokeStatus {
    /** Command is currently being executed. */
    @SerialName("RUNNING")
    RUNNING,

    /** Command has completed successfully. */
    @SerialName("COMPLETED")
    COMPLETED,

    /** Command execution failed. */
    @SerialName("FAILED")
    FAILED,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [InvokeStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [InvokeStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): InvokeStatus = when (value?.uppercase()) {
            "RUNNING" -> RUNNING
            "COMPLETED" -> COMPLETED
            "FAILED" -> FAILED
            else -> UNKNOWN
        }
    }
}

/**
 * Charger connection status.
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class ChargerConnectionStatus {
    /** Charger is connected to the vehicle. */
    @SerialName("CONNECTED")
    CONNECTED,

    /** Charger is disconnected from the vehicle. */
    @SerialName("DISCONNECTED")
    DISCONNECTED,

    /** Connection status is in a fault state. */
    @SerialName("FAULT")
    FAULT,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [ChargerConnectionStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [ChargerConnectionStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): ChargerConnectionStatus = when (value?.uppercase()) {
            "CONNECTED" -> CONNECTED
            "DISCONNECTED" -> DISCONNECTED
            "FAULT" -> FAULT
            else -> UNKNOWN
        }
    }
}

/**
 * Vehicle charging status.
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class ChargingStatus {
    /** Vehicle is not charging. */
    @SerialName("IDLE")
    IDLE,

    /** Vehicle is currently charging. */
    @SerialName("CHARGING")
    CHARGING,

    /** Charging is complete. */
    @SerialName("DONE")
    DONE,

    /** Charging has faulted. */
    @SerialName("FAULT")
    FAULT,

    /** Charging is scheduled for later. */
    @SerialName("SCHEDULED")
    SCHEDULED,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [ChargingStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [ChargingStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): ChargingStatus = when (value?.uppercase()) {
            "IDLE" -> IDLE
            "CHARGING" -> CHARGING
            "DONE" -> DONE
            "FAULT" -> FAULT
            "SCHEDULED" -> SCHEDULED
            else -> UNKNOWN
        }
    }
}

/**
 * Type of charging being performed.
 *
 * Note: The API may return additional type values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class ChargingType {
    /** No charging is occurring. */
    @SerialName("NONE")
    NONE,

    /** AC (Alternating Current) charging. */
    @SerialName("AC")
    AC,

    /** DC (Direct Current) fast charging. */
    @SerialName("DC")
    DC,

    /** Unknown type value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [ChargingType], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [ChargingType] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): ChargingType = when (value?.uppercase()) {
            "NONE" -> NONE
            "AC" -> AC
            "DC" -> DC
            else -> UNKNOWN
        }
    }
}

/**
 * Resource data status (used in Energy API responses).
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class ResourceStatus {
    /** Data is available and valid. */
    @SerialName("OK")
    OK,

    /** An error occurred fetching the data. */
    @SerialName("ERROR")
    ERROR,

    /** Data is not available. */
    @SerialName("NOT_AVAILABLE")
    NOT_AVAILABLE,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [ResourceStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [ResourceStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): ResourceStatus = when (value?.uppercase()) {
            "OK" -> OK
            "ERROR" -> ERROR
            "NOT_AVAILABLE" -> NOT_AVAILABLE
            else -> UNKNOWN
        }
    }
}

/**
 * Engine status.
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class EngineRunningStatus {
    /** Engine is stopped. */
    @SerialName("STOPPED")
    STOPPED,

    /** Engine is running. */
    @SerialName("RUNNING")
    RUNNING,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [EngineRunningStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [EngineRunningStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): EngineRunningStatus = when (value?.uppercase()) {
            "STOPPED" -> STOPPED
            "RUNNING" -> RUNNING
            else -> UNKNOWN
        }
    }
}

/**
 * Door/window status values.
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class OpenCloseStatus {
    /** Component is open. */
    @SerialName("OPEN")
    OPEN,

    /** Component is closed. */
    @SerialName("CLOSED")
    CLOSED,

    /** Component is ajar (partially open). */
    @SerialName("AJAR")
    AJAR,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [OpenCloseStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [OpenCloseStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): OpenCloseStatus = when (value?.uppercase()) {
            "OPEN" -> OPEN
            "CLOSED" -> CLOSED
            "AJAR" -> AJAR
            else -> UNKNOWN
        }
    }
}

/**
 * Lock status.
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class LockStatus {
    /** Vehicle/component is locked. */
    @SerialName("LOCKED")
    LOCKED,

    /** Vehicle/component is unlocked. */
    @SerialName("UNLOCKED")
    UNLOCKED,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [LockStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [LockStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): LockStatus = when (value?.uppercase()) {
            "LOCKED" -> LOCKED
            "UNLOCKED" -> UNLOCKED
            else -> UNKNOWN
        }
    }
}

/**
 * Warning status.
 *
 * Note: The API may return additional status values in the future.
 * Always handle the [UNKNOWN] case for forward compatibility.
 */
@Serializable
public enum class WarningStatus {
    /** No warning present. */
    @SerialName("NO_WARNING")
    NO_WARNING,

    /** Warning is present. */
    @SerialName("WARNING")
    WARNING,

    /** Service is required. */
    @SerialName("SERVICE_REQUIRED")
    SERVICE_REQUIRED,

    /** Unknown status value (forward compatibility). */
    UNKNOWN;

    public companion object {
        /**
         * Safely parses a string to [WarningStatus], returning [UNKNOWN] for unrecognized values.
         *
         * @param value The string value to parse.
         * @return The corresponding [WarningStatus] or [UNKNOWN] if not recognized.
         */
        public fun fromString(value: String?): WarningStatus = when (value?.uppercase()) {
            "NO_WARNING" -> NO_WARNING
            "WARNING" -> WARNING
            "SERVICE_REQUIRED" -> SERVICE_REQUIRED
            else -> UNKNOWN
        }
    }
}
