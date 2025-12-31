package com.example.hw_3.data.repository

import com.example.hw_3.api.DnDApiService
import com.example.hw_3.data.mapper.toEntity
import com.example.hw_3.domain.entity.ClassEntity
import com.example.hw_3.domain.entity.MonsterEntity
import com.example.hw_3.domain.entity.ReferenceEntity
import com.example.hw_3.domain.entity.SpellEntity
import com.example.hw_3.domain.repository.DnDRepository
import java.io.IOException

class DnDRepositoryImpl(
    private val apiService: DnDApiService
) : DnDRepository {

    override suspend fun getClasses(): Result<List<ReferenceEntity>> {
        return try {
            val response = apiService.getClasses()
            if (response.isSuccessful) {
                val entities = response.body()?.results?.map { it.toEntity() } ?: emptyList()
                Result.success(entities)
            } else {
                Result.failure(Exception("Ошибка загрузки классов: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Неизвестная ошибка: ${e.message}", e))
        }
    }

    override suspend fun getClassDetails(index: String): Result<ClassEntity> {
        return try {
            val response = apiService.getClassDetails(index)
            if (response.isSuccessful) {
                val entity = response.body()?.toEntity()
                    ?: return Result.failure(Exception("Пустой ответ от сервера"))
                Result.success(entity)
            } else {
                Result.failure(Exception("Ошибка загрузки класса: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Неизвестная ошибка: ${e.message}", e))
        }
    }

    override suspend fun getSpells(level: Int?, school: String?): Result<List<ReferenceEntity>> {
        return try {
            val response = apiService.getSpells(level = level, school = school)
            if (response.isSuccessful) {
                val entities = response.body()?.results?.map { it.toEntity() } ?: emptyList()
                Result.success(entities)
            } else {
                Result.failure(Exception("Ошибка загрузки заклинаний: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Неизвестная ошибка: ${e.message}", e))
        }
    }

    override suspend fun getSpellDetails(index: String): Result<SpellEntity> {
        return try {
            val response = apiService.getSpellDetails(index)
            if (response.isSuccessful) {
                val entity = response.body()?.toEntity()
                    ?: return Result.failure(Exception("Пустой ответ от сервера"))
                Result.success(entity)
            } else {
                Result.failure(Exception("Ошибка загрузки заклинания: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Неизвестная ошибка: ${e.message}", e))
        }
    }

    override suspend fun getMonsters(challengeRating: Double?): Result<List<ReferenceEntity>> {
        return try {
            val response = apiService.getMonsters(challengeRating = challengeRating)
            if (response.isSuccessful) {
                val entities = response.body()?.results?.map { it.toEntity() } ?: emptyList()
                Result.success(entities)
            } else {
                Result.failure(Exception("Ошибка загрузки монстров: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Неизвестная ошибка: ${e.message}", e))
        }
    }

    override suspend fun getMonsterDetails(index: String): Result<MonsterEntity> {
        return try {
            val response = apiService.getMonsterDetails(index)
            if (response.isSuccessful) {
                val entity = response.body()?.toEntity()
                    ?: return Result.failure(Exception("Пустой ответ от сервера"))
                Result.success(entity)
            } else {
                Result.failure(Exception("Ошибка загрузки монстра: ${response.code()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Ошибка сети: ${e.message}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Неизвестная ошибка: ${e.message}", e))
        }
    }
}

