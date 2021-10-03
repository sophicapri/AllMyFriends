package com.example.allmyfriends.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

@Parcelize
data class Dob (
	val date : String,
	val age : Int
): Parcelable {

	fun formattedDate() : String {
		val temporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(this.date)
		val instant = Instant.from(temporalAccessor)
		val date = Date.from(instant)
		val df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
		return df.format(date)
	}
}
