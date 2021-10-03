package com.example.allmyfriends.model.dto

import com.squareup.moshi.JsonClass
import kotlinx.parcelize.RawValue

@JsonClass(generateAdapter = true)
data class Name (
	val title : String,
	val first : String,
	val last : String
)
