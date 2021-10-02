/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.allmyfriends.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.model.Person
import com.example.android.codelabs.paging.db.RemoteKeys
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
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Person>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
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
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                db.remoteKeysDao().remoteKeysPersonId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Person>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { person ->
                // Get the remote keys of the first items retrieved
                db.remoteKeysDao().remoteKeysPersonId(person.id)
            }
    }
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
