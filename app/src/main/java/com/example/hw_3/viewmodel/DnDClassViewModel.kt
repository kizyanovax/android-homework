package com.example.hw_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.api.RetrofitClient
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.DnDClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DnDClassViewModel : ViewModel() {
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.dnDApiService.getClasses()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _classes.value = response.body()?.results ?: emptyList()
                        _error.value = null
                    } else {
                        _error.value = "Ошибка загрузки классов: ${response.code()}"
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = "Ошибка: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }

    fun fetchClassDetails(index: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.dnDApiService.getClassDetails(index)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _selectedClass.value = response.body()
                        _error.value = null
                    } else {
                        _error.value = "Ошибка загрузки класса: ${response.code()}"
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = "Ошибка: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }
}


