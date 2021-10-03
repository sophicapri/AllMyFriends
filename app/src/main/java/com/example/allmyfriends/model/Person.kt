package com.example.allmyfriends.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.allmyfriends.model.dto.Dob
import com.example.allmyfriends.model.dto.Location
import com.example.allmyfriends.model.dto.Name
import com.example.allmyfriends.model.dto.Picture
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: @RawValue Name,
    var picture: @RawValue Picture,
    var location: @RawValue Location,
    var nationality: String,
    val email: String,
    val dateOfBirth: @RawValue Dob,
    val cell: String,
    val phone: String,
    val gender: String,
): Parcelable