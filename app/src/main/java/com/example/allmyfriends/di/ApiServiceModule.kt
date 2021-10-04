package com.example.allmyfriends.di

import com.example.allmyfriends.data.remote.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
    private const val EXCLUDE_FIELD_KEY = "exc"
    private const val FIELDS_EXCLUDED = "registered, id, login"

    private fun initRequest() {
        httpClient.addInterceptor { chain ->
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url
            val url: HttpUrl = originalHttpUrl.newBuilder()
                .addQueryParameter(EXCLUDE_FIELD_KEY, FIELDS_EXCLUDED)
                .build()

            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    fun provideApiService(): ApiService {
        initRequest()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder().baseUrl(ApiService.API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient.build())
            .build()
            .create(ApiService::class.java)
    }
}