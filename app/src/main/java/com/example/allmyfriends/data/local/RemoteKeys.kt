package com.example.allmyfriends.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val personId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
