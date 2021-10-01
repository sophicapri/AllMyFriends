package com.example.allmyfriends.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.allmyfriends.model.Person

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeople(people: List<Person>): List<Long>

    @Query("SELECT * FROM people")
    fun getPeople(): PagingSource<Int, Person>

    @Query("DELETE FROM people")
    fun deleteAllUsers()
}