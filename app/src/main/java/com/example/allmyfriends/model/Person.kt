package com.example.allmyfriends.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.allmyfriends.model.dto.Dob
import com.example.allmyfriends.model.dto.Location
import com.example.allmyfriends.model.dto.Name
import com.example.allmyfriends.model.dto.Picture
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "people")
data class Person (
    @PrimaryKey(autoGenerate = true) var id: Long?,
    val gender : String,
    val name : Name,
    val location : Location,
    val email : String,
    val phone : String,
    val cell : String,
    val picture : Picture,
    @Json(name = "dob")val dateOfBirth : Dob,
    @Json(name = "nat")val nationality : String
): Parcelable
