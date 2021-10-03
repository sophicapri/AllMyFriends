package com.example.allmyfriends.di

import android.app.Application
import androidx.room.Room
import com.example.allmyfriends.data.TypeConverter
import com.example.allmyfriends.data.local.AllMyFriendsDatabase
import com.example.allmyfriends.data.local.AllMyFriendsDatabase.Companion.DATABASE_NAME
import com.example.allmyfriends.data.local.PersonDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Application): AllMyFriendsDatabase {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Room.databaseBuilder(context, AllMyFriendsDatabase::class.java, DATABASE_NAME)
            .addTypeConverter(TypeConverter(moshi))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePersonDao(database: AllMyFriendsDatabase): PersonDao {
        return database.personDao()
    }
}
