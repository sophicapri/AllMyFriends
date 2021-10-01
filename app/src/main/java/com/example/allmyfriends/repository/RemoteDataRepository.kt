package com.example.allmyfriends.repository

import com.example.allmyfriends.data.remote.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataRepository(private var service: ApiService/*, private var ioDispatcher : CoroutineDispatcher*/
) {

    fun getDataFromRemote() = flow { emit(service.queryData()) }//.flowOn(ioDispatcher)
}