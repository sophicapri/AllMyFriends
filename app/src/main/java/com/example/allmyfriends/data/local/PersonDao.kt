package com.example.allmyfriends.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.allmyfriends.model.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeople(people: List<Person>)

    @Query("SELECT * FROM people")
    fun getPeople(): PagingSource<Int, Person>

    @Query("DELETE FROM people")
    fun clearPeople()
}