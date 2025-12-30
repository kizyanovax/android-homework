package com.example.hw_3.api

import com.example.hw_3.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DnDApiService {
    // Получить список всех классов
    @GET("classes")
    suspend fun getClasses(): Response<ApiListResponse<ApiReference>>
    
    // Получить детали класса по индексу
    @GET("classes/{index}")
    suspend fun getClassDetails(@Path("index") index: String): Response<DnDClass>
    
    // Получить список всех заклинаний
    @GET("spells")
    suspend fun getSpells(): Response<ApiListResponse<ApiReference>>
    
    // Получить детали заклинания по индексу
    @GET("spells/{index}")
    suspend fun getSpellDetails(@Path("index") index: String): Response<DnDSpell>
    
    // Получить список всех монстров
    @GET("monsters")
    suspend fun getMonsters(): Response<ApiListResponse<ApiReference>>
    
    // Получить детали монстра по индексу
    @GET("monsters/{index}")
    suspend fun getMonsterDetails(@Path("index") index: String): Response<DnDMonster>
}


