package com.example.hw_3.domain.usecase

import com.example.hw_3.domain.entity.SpellEntity
import com.example.hw_3.domain.repository.DnDRepository

class GetSpellDetailsUseCase(
    private val repository: DnDRepository
) {
    suspend operator fun invoke(index: String): Result<SpellEntity> {
        return repository.getSpellDetails(index)
    }
}

