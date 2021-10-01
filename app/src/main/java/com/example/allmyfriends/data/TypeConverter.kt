package com.example.allmyfriends.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.allmyfriends.model.dto.Location
import com.example.allmyfriends.model.dto.Name
import com.example.allmyfriends.model.dto.Picture
import com.squareup.moshi.Moshi

@ProvidedTypeConverter
class TypeConverter(val moshi: Moshi) {

    @TypeConverter
    fun nameToJson(name: Name): String = moshi.adapter(Name::class.java).toJson(name)

    @TypeConverter
    fun jsonToName(json: String) = moshi.adapter(Name::class.java).fromJson(json)

    @TypeConverter
    fun locationToJson(location: Location): String = moshi.adapter(Location::class.java).toJson(location)

    @TypeConverter
    fun jsonToLocation(json: String) = moshi.adapter(Location::class.java).fromJson(json)

    @TypeConverter
    fun pictureToJson(picture: Picture): String = moshi.adapter(Picture::class.java).toJson(picture)

    @TypeConverter
    fun jsonToPicture(json: String) = moshi.adapter(Picture::class.java).fromJson(json)

}