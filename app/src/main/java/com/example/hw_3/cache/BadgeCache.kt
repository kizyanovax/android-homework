package com.example.hw_3.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Класс-кэш для хранения информации о необходимости показа бейджа.
 * Используется для экранов списка и настроек.
 * Информация не сохраняется долгосрочно, только в памяти приложения.
 */
class BadgeCache {
    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters: StateFlow<Boolean> = _hasActiveFilters.asStateFlow()

    fun setHasActiveFilters(hasFilters: Boolean) {
        _hasActiveFilters.value = hasFilters
    }
}


