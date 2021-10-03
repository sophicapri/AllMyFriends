package com.example.allmyfriends.model.dto

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Picture (
	val large : String,
	val medium : String,
	val thumbnail : String
): Parcelable
