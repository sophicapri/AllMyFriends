package com.example.allmyfriends.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.local.UserDao
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.User
import com.example.allmyfriends.util.Result
import com.example.allmyfriends.util.networkBoundResource
import kotlinx.coroutines.flow.Flow

class PeopleRepository(private var service: ApiService, var db: AllMyFriendsDatabase) {
    private var userDao: UserDao = db.personDao()

    fun getUsers(): Flow<Result<PagingData<User>>> = networkBoundResource(
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
            service.queryData().users.map { it.toDomainModel() }
        },
        saveFetchResult = { people ->
            db.withTransaction {
                userDao.deleteAllUsers()
                userDao.insertPeople(people)
            }
        }
    )

    companion object {
        private const val PAGE_SIZE = 20
    }
}