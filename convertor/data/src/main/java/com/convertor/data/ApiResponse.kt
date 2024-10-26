package com.convertor.data

sealed class ApiResponse<out T, out E> {
    data class Success<out T>(val data: T) : ApiResponse<T, Nothing>()
    data class Error<E>(val error: E) : ApiResponse<Nothing, E>()
    object Loading : ApiResponse<Nothing, Nothing>()
}