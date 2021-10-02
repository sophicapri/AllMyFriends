package com.example.allmyfriends.data.local

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import retrofit2.HttpException
import java.io.IOException

/*
*  Documentation : https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
*  Codelab : https://github.com/googlecodelabs/android-paging
*/

@OptIn(ExperimentalPagingApi::class)
class ApiServiceRemoteMediator(
    private val service: ApiService,
    private val db: AllMyFriendsDatabase
) : RemoteMediator<Int, Person>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Person>): MediatorResult {
        Log.d("RemoteMediator", "load: ")
        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        Log.d("ApiRemoteMediator", "load: page = $page")

        try {

            val apiResponse = service.queryData(page, state.config.pageSize)

            val people = apiResponse.users.map { it.toDomain() }
            val endOfPaginationReached = people.isEmpty()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.personDao().clearUsers()
                }
                val prevKey = if (page == STARTING_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = people.map {
                    RemoteKeys(personId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                db.remoteKeysDao().insertAll(keys)
                db.personDao().insertPeople(people)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Person>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { person ->
                // Get the remote keys of the last item retrieved
                db.remoteKeysDao().remoteKeysPersonId(person.id)
            }
    }

 /*   private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Person>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { person ->
                // Get the remote keys of the first items retrieved
                db.remoteKeysDao().remoteKeysPersonId(person.id)
            }
    }*/
/*
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Person>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.remoteKeysDao().remoteKeysPersonId(repoId)
            }
        }
    }*/

    companion object {
        private const val STARTING_PAGE = 0
    }
}
