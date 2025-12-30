package com.example.hw_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.api.RetrofitClient
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.DnDMonster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DnDMonsterViewModel : ViewModel() {
    private val _monsters = MutableStateFlow<List<ApiReference>>(emptyList())
    val monsters: StateFlow<List<ApiReference>> = _monsters.asStateFlow()

    private val _selectedMonster = MutableStateFlow<DnDMonster?>(null)
    val selectedMonster: StateFlow<DnDMonster?> = _selectedMonster.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchMonsters() {
        if (_monsters.value.isNotEmpty()) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.dnDApiService.getMonsters()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _monsters.value = response.body()?.results ?: emptyList()
                        _error.value = null
                    } else {
                        _error.value = "Ошибка загрузки монстров: ${response.code()}"
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

    fun fetchMonsterDetails(index: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.dnDApiService.getMonsterDetails(index)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _selectedMonster.value = response.body()
                        _error.value = null
                    } else {
                        _error.value = "Ошибка загрузки монстра: ${response.code()}"
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


