package com.example.allmyfriends.data.remote

import com.example.allmyfriends.model.dto.ApiResponse
import retrofit2.http.GET

interface ApiService {

    @GET("api/?results=5000&seed=myset")
    suspend fun queryData(): ApiResponse

        companion object{
        const val API_URL = " https://randomuser.me/"
    }
}