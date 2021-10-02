package com.example.allmyfriends.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.local.ApiServiceRemoteMediator
import com.example.allmyfriends.data.local.UserDao
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.util.Result
import com.example.allmyfriends.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class PeopleRepository(private var service: ApiService, var db: AllMyFriendsDatabase) {
    private var userDao: UserDao = db.personDao()

 /*      fun getUsers(): Flow<Result<PagingData<Person>>> = networkBoundResource(
           query = {
               val pagingSourceFactory = { userDao.getPeople() }
               Pager(
                   config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true),
                   pagingSourceFactory = pagingSourceFactory
               ).flow
           },
           shouldFetch = {
               //if (internetAvailable())
               true
           },
           fetch = {
               service.queryData().users.map { it.toDomain() }
           },
           saveFetchResult = { people ->
               db.withTransaction {
                   userDao.deleteAllUsers()
                   userDao.insertPeople(people)
               }

           }
       )*/

    fun getUsers(): Flow<PagingData<Person>> {
        //val pagingSourceFactory = { userDao.getPeople() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = ApiServiceRemoteMediator(service, db),
            pagingSourceFactory = { UserRemotePagingSource(service, db) }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}