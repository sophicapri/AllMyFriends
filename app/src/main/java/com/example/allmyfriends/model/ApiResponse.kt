package com.example.allmyfriends.model

import com.squareup.moshi.Json

data class ApiResponse (
    @Json(name = "results") val people : List<Person>,
    val info : Info
)
