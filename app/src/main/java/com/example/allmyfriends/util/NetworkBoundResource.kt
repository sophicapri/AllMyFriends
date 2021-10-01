package com.example.allmyfriends.util

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <LocalDataType, RemoteDataType> networkBoundResource(
    crossinline query: () -> Flow<LocalDataType>,
    crossinline shouldFetch: () -> Boolean,
    crossinline fetch: suspend () -> RemoteDataType,
    crossinline saveFetchResult: suspend (RemoteDataType) -> Unit
) = flow {

    val flow = if (shouldFetch()) {
        //emit(Result.Loading())
        try {
            saveFetchResult(fetch())
            query().map { Result.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Result.Error(throwable, it) }
        }
    } else {
        query().map { Result.Success(it) }
    }

    emitAll(flow)
}