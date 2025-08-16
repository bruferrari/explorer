package com.ferrarib.explorer.core.utils

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A wrapper around Android's Log class with automatic tagging.
 */
@Singleton
class AppLogger @Inject constructor() {

    /**
     * Sends a DEBUG log message with automatic tag from calling class.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun d(message: String, throwable: Throwable? = null) {
        Log.d(getCallerTag(), message, throwable)
    }

    /**
     * Sends an ERROR log message with automatic tag from calling class.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun e(message: String, throwable: Throwable? = null) {
        Log.e(getCallerTag(), message, throwable)
    }

    /**
     * Sends an INFO log message with automatic tag from calling class.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun i(message: String, throwable: Throwable? = null) {
        Log.i(getCallerTag(), message, throwable)
    }

    /**
     * Sends a WARN log message with automatic tag from calling class.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun w(message: String, throwable: Throwable? = null) {
        Log.w(getCallerTag(), message, throwable)
    }

    /**
     * Sends a VERBOSE log message with automatic tag from calling class.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun v(message: String, throwable: Throwable? = null) {
        Log.v(getCallerTag(), message, throwable)
    }

    // Legacy methods with explicit tags for backward compatibility
    
    /**
     * Sends a DEBUG log message with explicit tag.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun d(tag: String, message: String, throwable: Throwable? = null) {
        Log.d(tag, message, throwable)
    }

    /**
     * Sends an ERROR log message with explicit tag.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }

    /**
     * Sends an INFO log message with explicit tag.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun i(tag: String, message: String, throwable: Throwable? = null) {
        Log.i(tag, message, throwable)
    }

    /**
     * Sends a WARN log message with explicit tag.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w(tag, message, throwable)
    }

    /**
     * Sends a VERBOSE log message with explicit tag.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun v(tag: String, message: String, throwable: Throwable? = null) {
        Log.v(tag, message, throwable)
    }

    /**
     * Gets the simple class name of the caller for use as a log tag.
     * 
     * Uses stack trace inspection to find the first stack frame that is not
     * part of the AppLogger class or Thread utilities. Extracts the simple
     * class name and truncates it to Android's 23-character log tag limit.
     * 
     * @return The simple class name of the caller, or "Unknown" if detection fails
     */
    private fun getCallerTag(): String {
        return try {
            val stackTrace = Thread.currentThread().stackTrace
            // Find the first stack frame that's not this class or Thread
            val callerFrame = stackTrace.firstOrNull { frame ->
                !frame.className.contains("AppLogger") &&
                !frame.className.contains("Thread") &&
                !frame.methodName.contains("getStackTrace")
            }
            
            callerFrame?.className?.substringAfterLast('.')?.take(23) ?: "Unknown"
        } catch (_: Exception) {
            "AppLogger"
        }
    }
}