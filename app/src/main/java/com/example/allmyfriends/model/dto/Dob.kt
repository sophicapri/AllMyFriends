package com.example.allmyfriends.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dob (
	val date : String,
	val age : Int
): Parcelable
