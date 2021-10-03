package com.example.allmyfriends.model.dto

import com.example.allmyfriends.model.Person
import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "results") val people: List<PersonDto>,
    val info: Info
)

fun ApiResponse.toPersonDomainModel() = this.people.map {
    Person(
        name = it.name,
        gender = it.gender,
        picture = it.picture,
        email = it.email,
        dateOfBirth = it.dob,
        cell = it.cell,
        phone = it.phone,
        location = it.location,
        nationality = it.nat
    )
}
