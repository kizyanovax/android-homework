package com.example.hw_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.DnDMonster
import com.example.hw_3.data.mapper.toDataModel
import com.example.hw_3.di.DnDModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DnDMonsterViewModel : ViewModel() {
    private val getMonstersUseCase = DnDModule.getMonstersUseCase
    private val getMonsterDetailsUseCase = DnDModule.getMonsterDetailsUseCase

    private val _monsters = MutableStateFlow<List<ApiReference>>(emptyList())
    val monsters: StateFlow<List<ApiReference>> = _monsters.asStateFlow()

    private val _selectedMonster = MutableStateFlow<DnDMonster?>(null)
    val selectedMonster: StateFlow<DnDMonster?> = _selectedMonster.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchMonsters(challengeRating: Double? = null) {
        if (_monsters.value.isNotEmpty() && challengeRating == null) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getMonstersUseCase(challengeRating = challengeRating).fold(
                onSuccess = { entities ->
                    _monsters.value = entities.map { it.toDataModel() }
                    _error.value = null
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Неизвестная ошибка"
                    _isLoading.value = false
                }
            )
        }
    }

    fun fetchMonsterDetails(index: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getMonsterDetailsUseCase(index).fold(
                onSuccess = { entity ->
                    _selectedMonster.value = entity.toDataModel()
                    _error.value = null
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Неизвестная ошибка"
                    _isLoading.value = false
                }
            )
        }
    }
}

