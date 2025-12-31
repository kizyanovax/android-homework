package com.example.hw_3.domain.usecase

import com.example.hw_3.domain.entity.ClassEntity
import com.example.hw_3.domain.repository.DnDRepository

class GetClassDetailsUseCase(
    private val repository: DnDRepository
) {
    suspend operator fun invoke(index: String): Result<ClassEntity> {
        return repository.getClassDetails(index)
    }
}

