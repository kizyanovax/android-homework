package com.example.hw_3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.api.RetrofitClient
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.DnDSpell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DnDSpellViewModel : ViewModel() {
    private val _spells = MutableStateFlow<List<ApiReference>>(emptyList())
    val spells: StateFlow<List<ApiReference>> = _spells.asStateFlow()

    private val _selectedSpell = MutableStateFlow<DnDSpell?>(null)
    val selectedSpell: StateFlow<DnDSpell?> = _selectedSpell.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchSpells() {
        if (_spells.value.isNotEmpty()) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.dnDApiService.getSpells()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _spells.value = response.body()?.results ?: emptyList()
                        _error.value = null
                    } else {
                        _error.value = "Ошибка загрузки заклинаний: ${response.code()}"
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

    fun fetchSpellDetails(index: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.dnDApiService.getSpellDetails(index)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _selectedSpell.value = response.body()
                        _error.value = null
                    } else {
                        _error.value = "Ошибка загрузки заклинания: ${response.code()}"
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


