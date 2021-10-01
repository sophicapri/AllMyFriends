package com.example.allmyfriends.model.dto

import com.squareup.moshi.Json

data class ApiResponse (
    @Json(name = "results") val people : List<PersonDto>,
    val info : Info
)
