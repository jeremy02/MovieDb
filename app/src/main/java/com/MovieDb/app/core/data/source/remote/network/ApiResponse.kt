package com.MovieDb.app.core.data.source.remote.network

import org.jetbrains.annotations.Contract

data class ApiResponse<T>(
    val status: StatusResponse,
    val body: T?,
    val message: String?
) {
    companion object {
        @JvmStatic
        @Contract(value = "_ -> new", pure = true)
        fun <T> success(body: T?): ApiResponse<T?> {
            return ApiResponse(StatusResponse.SUCCESS, body, null)
        }

        @Contract(value = "_, _ -> new", pure = true)
        fun <T> empty(msg: String?, body: T?): ApiResponse<T?> {
            return ApiResponse(StatusResponse.EMPTY, body, msg)
        }

        @Contract(value = "_, _ -> new", pure = true)
        fun <T> error(msg: String?, body: T?): ApiResponse<T?> {
            return ApiResponse(StatusResponse.ERROR, body, msg)
        }
    }
}