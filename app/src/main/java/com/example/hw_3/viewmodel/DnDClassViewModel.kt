package com.example.hw_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.DnDClass
import com.example.hw_3.data.mapper.toDataModel
import com.example.hw_3.di.DnDModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DnDClassViewModel : ViewModel() {
    private val getClassesUseCase = DnDModule.getClassesUseCase
    private val getClassDetailsUseCase = DnDModule.getClassDetailsUseCase

    private val _classes = MutableStateFlow<List<ApiReference>>(emptyList())
    val classes: StateFlow<List<ApiReference>> = _classes.asStateFlow()

    private val _selectedClass = MutableStateFlow<DnDClass?>(null)
    val selectedClass: StateFlow<DnDClass?> = _selectedClass.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchClasses() {
        if (_classes.value.isNotEmpty()) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getClassesUseCase().fold(
                onSuccess = { entities ->
                    _classes.value = entities.map { it.toDataModel() }
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

    fun fetchClassDetails(index: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            getClassDetailsUseCase(index).fold(
                onSuccess = { entity ->
                    _selectedClass.value = entity.toDataModel()
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

