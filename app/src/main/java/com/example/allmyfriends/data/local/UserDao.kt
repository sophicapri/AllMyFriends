package com.example.allmyfriends.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.example.allmyfriends.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeople(users: List<User>)

    @Query("SELECT * FROM people")
    fun getPeople(): PagingSource<Int, User>

    @Query("DELETE FROM people")
    fun deleteAllUsers()
}