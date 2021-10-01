package com.example.allmyfriends.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.allmyfriends.model.Person

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeople(people: List<Person>): List<Long>

    @Query("SELECT * FROM people")
    fun getPeople(): PagingSource<Int, Person>
}