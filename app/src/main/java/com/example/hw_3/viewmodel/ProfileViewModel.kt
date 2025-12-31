package com.example.hw_3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hw_3.data.preferences.Profile
import com.example.hw_3.data.preferences.ProfilePreferencesManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profilePreferencesManager = ProfilePreferencesManager(application)

    val profile: StateFlow<Profile> = profilePreferencesManager.profile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Profile()
        )

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            profilePreferencesManager.saveProfile(profile)
        }
    }
}

