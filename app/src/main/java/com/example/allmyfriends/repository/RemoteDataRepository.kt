package com.example.allmyfriends.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.allmyfriends.data.local.PersonDao
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.util.Result
import com.example.allmyfriends.util.networkBoundResource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataRepository(private var service: ApiService, var personDao: PersonDao) {

    fun getUsers(): Flow<Result<PagingData<Person>>> = networkBoundResource(
        query = {
            val pagingSourceFactory = { personDao.getPeople() }
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = pagingSourceFactory
            ).flow
        },
        shouldFetch = {
            //if (internetAvailable())
            true
        },
        fetch = {
            service.queryData().people.map { it.toDomain() }
        },
        saveFetchResult = { people ->
            personDao.insertPeople(people)
        }
    )
}