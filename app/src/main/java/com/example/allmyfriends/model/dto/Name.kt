package com.example.allmyfriends.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Name (
	val title : String,
	val first : String,
	val last : String
)
