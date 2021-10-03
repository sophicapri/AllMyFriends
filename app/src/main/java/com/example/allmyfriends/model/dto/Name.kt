package com.example.allmyfriends.model.dto

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class Name (
	val title : String,
	val first : String,
	val last : String
): Parcelable
