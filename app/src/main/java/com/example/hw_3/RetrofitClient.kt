package com.example.hw_3.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Используем D&D 5e API
    private const val BASE_URL = "https://www.dnd5eapi.co/api/2014/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClientProvider.client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val dnDApiService: DnDApiService by lazy {
        retrofit.create(DnDApiService::class.java)
    }
}