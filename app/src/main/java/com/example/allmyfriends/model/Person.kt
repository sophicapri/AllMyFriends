package com.example.allmyfriends.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.allmyfriends.model.dto.Dob
import com.example.allmyfriends.model.dto.Location
import com.example.allmyfriends.model.dto.Name
import com.example.allmyfriends.model.dto.Picture

@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: Name,
    var picture: Picture,
    var location: Location,
    var nationality: String,
    val email: String,
    val dateOfBirth: Dob,
    val cell: String,
    val phone: String,
    val gender: String,
)