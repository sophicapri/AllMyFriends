package com.example.allmyfriends.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.local.UserDao
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.util.networkBoundResource
import kotlinx.coroutines.flow.Flow

class PeopleRepository(private var service: ApiService, var db: AllMyFriendsDatabase) {
    private var userDao: UserDao = db.personDao()

       fun getUsersLocal(): Flow<PagingData<Person>> {
           val pagingSourceFactory = { userDao.getPeople() }
           return Pager(
               config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true),
               pagingSourceFactory = pagingSourceFactory
           ).flow
       }

    fun getUsers(): Flow<PagingData<Person>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true,  initialLoadSize = PAGE_SIZE * 3),
            pagingSourceFactory = { UserPagingSource(apiService = service, db) }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}