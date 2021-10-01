package com.example.allmyfriends.model

import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
data class Dob (
	val date : String,
	val age : Int
)
