package com.example.hw_3.profile.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_preferences")

data class Profile(
    val fullName: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = "",
    val position: String = "",
    val favoriteClassTime: String? = null
)

class ProfilePreferencesManager(private val context: Context) {
    private val fullNameKey = stringPreferencesKey("full_name")
    private val avatarUriKey = stringPreferencesKey("avatar_uri")
    private val resumeUrlKey = stringPreferencesKey("resume_url")
    private val positionKey = stringPreferencesKey("position")
    private val favoriteClassTimeKey = stringPreferencesKey("favorite_class_time")

    val profile: Flow<Profile> = context.profileDataStore.data.map { preferences ->
        Profile(
            fullName = preferences[fullNameKey] ?: "",
            avatarUri = preferences[avatarUriKey],
            resumeUrl = preferences[resumeUrlKey] ?: "",
            position = preferences[positionKey] ?: "",
            favoriteClassTime = preferences[favoriteClassTimeKey]
        )
    }

    suspend fun saveProfile(profile: Profile) {
        context.profileDataStore.edit { preferences ->
            preferences[fullNameKey] = profile.fullName
            if (profile.avatarUri != null) {
                preferences[avatarUriKey] = profile.avatarUri
            } else {
                preferences.remove(avatarUriKey)
            }
            preferences[resumeUrlKey] = profile.resumeUrl
            preferences[positionKey] = profile.position
            if (profile.favoriteClassTime != null) {
                preferences[favoriteClassTimeKey] = profile.favoriteClassTime
            } else {
                preferences.remove(favoriteClassTimeKey)
            }
        }
    }
}

