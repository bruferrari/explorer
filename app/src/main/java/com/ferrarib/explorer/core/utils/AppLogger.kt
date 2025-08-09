package com.ferrarib.explorer.core.utils

import android.util.Log

/**
 * A wrapper around Android's Log class.
 */
class AppLogger {

    /**
     * Sends a DEBUG log message.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun d(tag: String, message: String, throwable: Throwable? = null) {
        Log.d(tag, message, throwable)
    }

    /**
     * Sends an ERROR log message.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }

    /**
     * Sends an INFO log message.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun i(tag: String, message: String, throwable: Throwable? = null) {
        Log.i(tag, message, throwable)
    }

    /**
     * Sends a WARN log message.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w(tag, message, throwable)
    }

    /**
     * Sends a VERBOSE log message.
     * @param tag Used to identify the source of a log message.
     * @param message The message you would like logged.
     * @param throwable An optional throwable to log.
     */
    fun v(tag: String, message: String, throwable: Throwable? = null) {
        Log.v(tag, message, throwable)
    }
}
