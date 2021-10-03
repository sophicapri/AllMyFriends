package com.example.allmyfriends.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import com.example.allmyfriends.model.dto.toPersonDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PeopleRemotePagingSource @Inject
constructor(private val apiService: ApiService, var db: AllMyFriendsDatabase) :
    PagingSource<Int, Person>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val position = params.key ?: STARTING_INDEX
        return try {
            val job = SupervisorJob()
            val ioScope = CoroutineScope(Dispatchers.IO + job)

            val people = apiService.queryData(position, params.loadSize).toPersonDomainModel()

            // Cache data in RoomDatabase
            ioScope.launch {
                db.withTransaction {
                    if (params.key == STARTING_INDEX)
                        db.personDao().clearPeople()
                    db.personDao().insertPeople(people)
                }
            }

            LoadResult.Page(
                data = people,
                prevKey = if (position == STARTING_INDEX) null else position - 1,
                nextKey = if (people.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_INDEX = 1
        private const val TAG = "PeoplePagingSource"
    }
}
