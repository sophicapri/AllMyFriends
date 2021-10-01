package com.example.allmyfriends.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location (
    val street : Street,
    val city : String,
    val state : String,
    val postcode : String,
    val coordinates : Coordinates,
    val timezone : Timezone
)
