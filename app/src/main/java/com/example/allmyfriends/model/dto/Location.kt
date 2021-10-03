package com.example.allmyfriends.model.dto

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class Location (
    val street : Street,
    val city : String,
    val state : String,
    val postcode : String,
    val coordinates : Coordinates,
    val timezone : Timezone
): Parcelable
