package com.uzair.composeBase.utils

import com.uzair.composeBase.data.room_database.ResponseError


sealed class Resource<out T> {

    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val status: Status, val data: T?, val message: String?,val responseError: ResponseError?) : Resource<T>()

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }


    companion object {

        /**
         * Returns [State.Loading] instance.
         */
        fun <T> loading() = Loading<T>()

        /**
         * Returns [State.Success] instance.
         * @param data Data to emit with status.
         */
        fun <T> success(data: T) =
            Success(data)

        /**
         * Returns [State.Error] instance.
         * @param message Description of failure.
         */
        fun <T> error(message: String, data: T? = null, responseError: ResponseError?) =
            Error<T>(Status.ERROR, data, message,responseError)

    }



}