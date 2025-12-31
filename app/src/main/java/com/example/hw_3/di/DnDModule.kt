package com.example.hw_3.di

import com.example.hw_3.api.DnDApiService
import com.example.hw_3.api.RetrofitClient
import com.example.hw_3.data.repository.DnDRepositoryImpl
import com.example.hw_3.domain.repository.DnDRepository
import com.example.hw_3.domain.usecase.*

object DnDModule {
    private val apiService: DnDApiService by lazy {
        RetrofitClient.dnDApiService
    }

    private val repository: DnDRepository by lazy {
        DnDRepositoryImpl(apiService)
    }

    val getClassesUseCase: GetClassesUseCase by lazy {
        GetClassesUseCase(repository)
    }

    val getClassDetailsUseCase: GetClassDetailsUseCase by lazy {
        GetClassDetailsUseCase(repository)
    }

    val getSpellsUseCase: GetSpellsUseCase by lazy {
        GetSpellsUseCase(repository)
    }

    val getSpellDetailsUseCase: GetSpellDetailsUseCase by lazy {
        GetSpellDetailsUseCase(repository)
    }

    val getMonstersUseCase: GetMonstersUseCase by lazy {
        GetMonstersUseCase(repository)
    }

    val getMonsterDetailsUseCase: GetMonsterDetailsUseCase by lazy {
        GetMonsterDetailsUseCase(repository)
    }
}

