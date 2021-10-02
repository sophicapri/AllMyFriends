package com.example.allmyfriends.repository

import androidx.room.withTransaction
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.local.UserDao
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.User
import com.example.allmyfriends.model.dto.Info
import com.example.allmyfriends.util.Result
import com.example.allmyfriends.util.networkBoundResource
import kotlinx.coroutines.flow.Flow

class PeopleRepository(private var service: ApiService, var db: AllMyFriendsDatabase) {
    private var userDao: UserDao = db.personDao()

    fun getUsers(page: Int): Flow<Result<List<User>>> = networkBoundResource(
        query = { userDao.getUsers() },
        shouldFetch = {
            //if (internetAvailable())
            true
        },
        fetch = {
            val apiResponse = service.queryData(page)
            val info = apiResponse.info
            apiResponse.users.map { it.toDomainModel(info.page) }
        },
        saveFetchResult = { people ->
            db.withTransaction {
                if (page == 1)
                    userDao.deleteAllUsers()
                userDao.insertUsers(people)
            }
        }
    )
}