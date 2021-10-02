
package com.example.allmyfriends.repository

/*
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

class UserRemotePagingSource @Inject
constructor(private val apiService: ApiService, var db: AllMyFriendsDatabase) :
    PagingSource<Int, Person>() {
    companion object {
        const val STARTING_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val position = params.key ?: STARTING_INDEX
        return try {
            var showsList: List<Person>
            showsList = apiService.queryData(position).users.map { it.toDomain() }
            CoroutineScope(Dispatchers.IO).launch {
                db.withTransaction {
                    if (params.key == STARTING_INDEX)
                        db.personDao().clearUsers()
                    db.personDao().insertPeople(showsList)
                }
            }

            LoadResult.Page(
                data = showsList,
                prevKey = if (position == STARTING_INDEX) null else position - 1,
                nextKey = if (showsList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        TODO("Not yet implemented")
    }
}*/
