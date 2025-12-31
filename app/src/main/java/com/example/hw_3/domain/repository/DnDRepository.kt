package com.example.hw_3.domain.repository

import com.example.hw_3.domain.entity.ClassEntity
import com.example.hw_3.domain.entity.MonsterEntity
import com.example.hw_3.domain.entity.ReferenceEntity
import com.example.hw_3.domain.entity.SpellEntity

interface DnDRepository {
    suspend fun getClasses(): Result<List<ReferenceEntity>>
    suspend fun getClassDetails(index: String): Result<ClassEntity>
    
    suspend fun getSpells(level: Int? = null, school: String? = null): Result<List<ReferenceEntity>>
    suspend fun getSpellDetails(index: String): Result<SpellEntity>
    
    suspend fun getMonsters(challengeRating: Double? = null): Result<List<ReferenceEntity>>
    suspend fun getMonsterDetails(index: String): Result<MonsterEntity>
}

