package com.example.allmyfriends.data.remote

import com.example.allmyfriends.model.dto.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // To get new users on each SwipeRefresh, remove "seed" parameter
    @GET("api/?seed=myset")
    suspend fun queryData(
        @Query("page")page: Int,
        @Query("results")pageSize: Int
    ): ApiResponse

        companion object{
        const val API_URL = " https://randomuser.me/"
    }
}