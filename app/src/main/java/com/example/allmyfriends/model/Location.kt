package com.example.allmyfriends.model

data class Location (
	val street : Street,
	val city : String,
	val state : String,
	val postcode : Int,
	val coordinates : Coordinates,
	val timezone : Timezone
)
