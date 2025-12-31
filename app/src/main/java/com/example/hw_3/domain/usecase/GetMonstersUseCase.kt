package com.example.hw_3.domain.usecase

import com.example.hw_3.domain.entity.ReferenceEntity
import com.example.hw_3.domain.repository.DnDRepository

class GetMonstersUseCase(
    private val repository: DnDRepository
) {
    suspend operator fun invoke(challengeRating: Double? = null): Result<List<ReferenceEntity>> {
        return repository.getMonsters(challengeRating = challengeRating)
    }
}

