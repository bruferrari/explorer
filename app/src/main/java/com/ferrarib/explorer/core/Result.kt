package com.ferrarib.explorer.core

/**
 * A simple wrapper for handling success and error states.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    
    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> error(exception: Throwable): Result<T> = Error(exception)
    }
}