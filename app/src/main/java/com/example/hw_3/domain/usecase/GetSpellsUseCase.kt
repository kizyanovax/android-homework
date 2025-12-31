package com.example.hw_3.domain.usecase

import com.example.hw_3.domain.entity.ReferenceEntity
import com.example.hw_3.domain.repository.DnDRepository

class GetSpellsUseCase(
    private val repository: DnDRepository
) {
    suspend operator fun invoke(level: Int? = null, school: String? = null): Result<List<ReferenceEntity>> {
        return repository.getSpells(level = level, school = school)
    }
}

