package com.example.allmyfriends.data.remote

import com.example.allmyfriends.model.dto.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/?results=20&seed=myset")
    suspend fun queryData(@Query("page")page: Int): ApiResponse

        companion object{
        const val API_URL = " https://randomuser.me/"
    }
}