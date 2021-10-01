package com.example.allmyfriends.model.dto

import com.example.allmyfriends.model.Person


data class UserDto (
    val gender : String,
    val name : Name,
    val location : Location,
    val email : String,
    val login : Login,
    val dob : Dob,
    val registered : Registered,
    val phone : String,
    val id : Id,
    val cell : String,
    val picture : Picture,
    val nat : String
){
	fun toDomain() = Person(name = name, picture = picture, location = location, nationality = nat)
}
