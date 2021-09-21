package com.example.androidtemplate.data

/**
 * Encapsulates the state and outcome of an operation:
 *
 * * If operation returned successfully with resulting [value] of type [T] and [status] is [Status.SUCCESS],
 * * If operation failed with error [message] and [status] is [Status.FAILURE],
 * * If operation is ongoing and [status] is [Status.LOADING].
 */
class Result<T> private constructor(
    val status: Status,
    val value: T? = null,
    val message: String? = null
) {

    /**
     * `true` when operation was successful, `false` otherwise.
     */
    val isSuccess = status == Status.SUCCESS

    /**
     * `true` when operation failed, `false` otherwise.
     */
    val isFailure = status == Status.FAILURE

    companion object {
        /**
         * Creates a Result instance when operation is successful.
         * @param value The resulting value.
         */
        fun <T> success(value: T) = Result(Status.SUCCESS, value = value)

        /**
         * Creates a Result instance when operation fails.
         * @param message The error message.
         */
        fun <T> failure(message: String) = Result<T>(Status.FAILURE, message = message)

        /**
         * Creates a Result instance when operation hasn't finished.
         * @param value Potential resulting value.
         */
        fun <T> loading(value: T? = null) = Result(Status.LOADING, value = value)
    }
}

/**
 * States a [Result] object may find itself in.
 */
enum class Status {
    LOADING,
    SUCCESS,
    FAILURE
}
