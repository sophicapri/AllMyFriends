package com.example.allmyfriends.di

import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.remote.ApiService
import com.example.allmyfriends.repository.PeopleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRemoteDataRepository(apiService: ApiService, database : AllMyFriendsDatabase) =
        PeopleRepository(apiService, database)

    @Singleton
    @Provides
    fun provideMainCoroutineDispatcher() : CoroutineDispatcher = Dispatchers.Main
}