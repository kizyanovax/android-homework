package com.example.hw_3.domain.usecase

import com.example.hw_3.domain.entity.MonsterEntity
import com.example.hw_3.domain.repository.DnDRepository

class GetMonsterDetailsUseCase(
    private val repository: DnDRepository
) {
    suspend operator fun invoke(index: String): Result<MonsterEntity> {
        return repository.getMonsterDetails(index)
    }
}

