package com.example.allmyfriends.data.local

import androidx.room.*
import com.example.allmyfriends.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Query("SELECT * FROM people")
    fun getUsers(): Flow<List<User>>

    @Query("DELETE FROM people")
    fun deleteAllUsers()
}