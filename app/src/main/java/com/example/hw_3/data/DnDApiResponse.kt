package com.example.hw_3.data

import com.google.gson.annotations.SerializedName

// Базовая модель для ответов API со списком
data class ApiListResponse<T>(
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<T>
)

// Модель для элемента списка (классы, заклинания, монстры)
data class ApiReference(
    @SerializedName("index") val index: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)


