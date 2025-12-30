package com.example.hw_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.DnDSpell
import com.example.hw_3.data.mapper.toDataModel
import com.example.hw_3.di.DnDModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DnDSpellViewModel : ViewModel() {
    private val getSpellsUseCase = DnDModule.getSpellsUseCase
    private val getSpellDetailsUseCase = DnDModule.getSpellDetailsUseCase

    private val _spells = MutableStateFlow<List<ApiReference>>(emptyList())
    val spells: StateFlow<List<ApiReference>> = _spells.asStateFlow()

    private val _selectedSpell = MutableStateFlow<DnDSpell?>(null)
    val selectedSpell: StateFlow<DnDSpell?> = _selectedSpell.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchSpells(level: Int? = null, school: String? = null) {
        if (_spells.value.isNotEmpty() && level == null && school == null) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getSpellsUseCase(level = level, school = school).fold(
                onSuccess = { entities ->
                    _spells.value = entities.map { it.toDataModel() }
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

    fun fetchSpellDetails(index: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getSpellDetailsUseCase(index).fold(
                onSuccess = { entity ->
                    _selectedSpell.value = entity.toDataModel()
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

