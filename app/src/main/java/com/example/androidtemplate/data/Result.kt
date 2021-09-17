package com.example.androidtemplate.data

/**
 * Encapsulates the outcome of an operation:
 *
 * * If operation returned successfully with resulting [value] of type [T],
 * * If operation failed with error [message].
 */
class Result<T> private constructor(
    val value: T? = null,
    val message: String? = null
) {

    /**
     * `true` when operation was successful, `false` otherwise.
     */
    val isSuccess = value != null

    companion object {
        /**
         * Creates a Result instance when operation is successful.
         * @param value The resulting value.
         */
        fun <T> success(value: T) = Result(value = value)

        /**
         * Creates a Result instance when operation fails.
         * @param message The error message.
         */
        fun <T> failure(message: String) = Result<T>(message = message)
    }
}
