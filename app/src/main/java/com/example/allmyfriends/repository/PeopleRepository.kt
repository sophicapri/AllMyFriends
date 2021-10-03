package com.example.allmyfriends.repository

import androidx.paging.*
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.local.UserDao
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import kotlinx.coroutines.flow.Flow

class PeopleRepository(private var service: ApiService, var db: AllMyFriendsDatabase) {
    private var userDao: UserDao = db.personDao()

       fun getUsersLocal(): Flow<PagingData<Person>> {
           return Pager(
               config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
               pagingSourceFactory = { userDao.getPeople() }
           ).flow
       }

    fun getUsersRemote(): Flow<PagingData<Person>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { UserRemotePagingSource(apiService = service, db) }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}