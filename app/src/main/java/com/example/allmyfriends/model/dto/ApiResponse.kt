package com.example.allmyfriends.model.dto

import com.squareup.moshi.Json

data class ApiResponse (
    @Json(name = "results") val users : List<UserDto>,
    val info : Info
)
