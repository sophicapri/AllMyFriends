package com.example.allmyfriends.repository

import androidx.paging.*
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.local.PersonDao
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import kotlinx.coroutines.flow.Flow

class PeopleRepository(private var service: ApiService, var db: AllMyFriendsDatabase) {
    private var personDao: PersonDao = db.personDao()

       fun getPeopleLocal(): Flow<PagingData<Person>> {
           return Pager(
               config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
               pagingSourceFactory = { personDao.getPeople() }
           ).flow
       }

    fun getPeopleRemote(): Flow<PagingData<Person>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { PeopleRemotePagingSource(apiService = service, db) }
        ).flow
    }

    fun getPersonById(personId: Long) : Flow<Person>{
        return personDao.getPersonById(personId)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}