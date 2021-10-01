package com.example.allmyfriends.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.allmyfriends.data.TypeConverter
import com.example.allmyfriends.model.Person

@Database(entities = [Person::class], version = 2, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class AllMyFriendsDatabase: RoomDatabase() {
    abstract fun personDao(): PersonDao

    companion object{
        const val DATABASE_NAME = "AllMyFriends.db"
    }
}