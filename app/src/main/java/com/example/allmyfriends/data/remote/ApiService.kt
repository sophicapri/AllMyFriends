package com.example.allmyfriends.data.remote

import com.example.allmyfriends.model.ApiResponse
import retrofit2.http.GET

interface ApiService {

    @GET("api/?page=10&results=2&seed=myset")
    suspend fun queryData(): ApiResponse

        companion object{
        const val API_URL = " https://randomuser.me/"
    }
}