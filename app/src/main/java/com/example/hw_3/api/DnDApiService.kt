package com.example.hw_3.api

import com.example.hw_3.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DnDApiService {
    // Получить список всех классов
    @GET("classes")
    suspend fun getClasses(): Response<ApiListResponse<ApiReference>>
    
    // Получить детали класса по индексу
    @GET("classes/{index}")
    suspend fun getClassDetails(@Path("index") index: String): Response<DnDClass>
    
    // Получить список всех заклинаний с фильтрацией
    @GET("spells")
    suspend fun getSpells(
        @Query("level") level: Int? = null,
        @Query("school") school: String? = null
    ): Response<ApiListResponse<ApiReference>>
    
    // Получить детали заклинания по индексу
    @GET("spells/{index}")
    suspend fun getSpellDetails(@Path("index") index: String): Response<DnDSpell>
    
    // Получить список всех монстров с фильтрацией по рейтингу сложности
    @GET("monsters")
    suspend fun getMonsters(
        @Query("challenge_rating") challengeRating: Double? = null
    ): Response<ApiListResponse<ApiReference>>
    
    // Получить детали монстра по индексу
    @GET("monsters/{index}")
    suspend fun getMonsterDetails(@Path("index") index: String): Response<DnDMonster>
}

