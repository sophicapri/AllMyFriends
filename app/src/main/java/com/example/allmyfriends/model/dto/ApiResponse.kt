package com.example.allmyfriends.model.dto

import com.example.allmyfriends.model.Person
import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "results") val people: List<Person>,
    val info: Info
)