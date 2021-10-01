package com.example.allmyfriends.util

sealed class Result<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Loading<T> : Result<T>()
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Result<T>(data, throwable)
}