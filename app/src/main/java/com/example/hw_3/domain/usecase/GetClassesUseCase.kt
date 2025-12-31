package com.example.hw_3.domain.usecase

import com.example.hw_3.domain.entity.ReferenceEntity
import com.example.hw_3.domain.repository.DnDRepository

class GetClassesUseCase(
    private val repository: DnDRepository
) {
    suspend operator fun invoke(): Result<List<ReferenceEntity>> {
        return repository.getClasses()
    }
}

