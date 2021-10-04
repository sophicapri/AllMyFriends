package com.example.allmyfriends.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PeopleRemotePagingSource @Inject
constructor(private val apiService: ApiService, var db: AllMyFriendsDatabase) :
    PagingSource<Int, Person>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val people = apiService.queryData(position, params.loadSize).people

            cacheData(position, people)

            LoadResult.Page(
                data = people,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (people.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    // Cache data in RoomDatabase
    private fun cacheData(position: Int?, people: List<Person>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.withTransaction {
                if (position == STARTING_PAGE_INDEX)
                    db.personDao().clearPeople()
                db.personDao().insertPeople(people)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
