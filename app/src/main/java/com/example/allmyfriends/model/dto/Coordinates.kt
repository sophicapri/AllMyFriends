package com.example.allmyfriends.model.dto

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coordinates (
	val latitude : Double,
	val longitude : Double
): Parcelable
