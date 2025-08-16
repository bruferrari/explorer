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
    fun d(tag: String, message: String, throwable: Throwable? = null) {
        Log.d(tag, message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }

    fun i(tag: String, message: String, throwable: Throwable? = null) {
        Log.i(tag, message, throwable)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w(tag, message, throwable)
    }

    fun v(tag: String, message: String, throwable: Throwable? = null) {
        Log.v(tag, message, throwable)
    }

    /**
     * Gets the simple class name of the caller for use as a log tag.
     * This function extracts the calling class name from the stack trace.
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
        } catch (e: Exception) {
            "AppLogger"
        }
    }
}