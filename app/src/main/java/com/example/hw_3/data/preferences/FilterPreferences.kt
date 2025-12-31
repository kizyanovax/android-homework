package com.example.hw_3.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_preferences")

data class FilterSettings(
    val monsterType: String? = null,
    val minChallengeRating: Double? = null,
    val nameSearch: String? = null,
    val selectedClassName: String? = null // Для фильтрации классов
)

class FilterPreferencesManager(private val context: Context) {
    private val monsterTypeKey = stringPreferencesKey("monster_type")
    private val minChallengeRatingKey = stringPreferencesKey("min_challenge_rating")
    private val nameSearchKey = stringPreferencesKey("name_search")
    private val selectedClassNameKey = stringPreferencesKey("selected_class_name")

    val filterSettings: Flow<FilterSettings> = context.dataStore.data.map { preferences ->
        FilterSettings(
            monsterType = preferences[monsterTypeKey],
            minChallengeRating = preferences[minChallengeRatingKey]?.toDoubleOrNull(),
            nameSearch = preferences[nameSearchKey],
            selectedClassName = preferences[selectedClassNameKey]
        )
    }

    suspend fun saveFilterSettings(settings: FilterSettings) {
        context.dataStore.edit { preferences ->
            if (settings.monsterType != null) {
                preferences[monsterTypeKey] = settings.monsterType
            } else {
                preferences.remove(monsterTypeKey)
            }
            
            if (settings.minChallengeRating != null) {
                preferences[minChallengeRatingKey] = settings.minChallengeRating.toString()
            } else {
                preferences.remove(minChallengeRatingKey)
            }
            
            if (settings.nameSearch != null && settings.nameSearch.isNotBlank()) {
                preferences[nameSearchKey] = settings.nameSearch
            } else {
                preferences.remove(nameSearchKey)
            }
            
            if (settings.selectedClassName != null && settings.selectedClassName.isNotBlank()) {
                preferences[selectedClassNameKey] = settings.selectedClassName
            } else {
                preferences.remove(selectedClassNameKey)
            }
        }
    }

    suspend fun clearFilters() {
        context.dataStore.edit { preferences ->
            preferences.remove(monsterTypeKey)
            preferences.remove(minChallengeRatingKey)
            preferences.remove(nameSearchKey)
            preferences.remove(selectedClassNameKey)
        }
    }
    
    suspend fun clearClassFilters() {
        context.dataStore.edit { preferences ->
            preferences.remove(selectedClassNameKey)
        }
    }

    fun hasActiveFiltersFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[monsterTypeKey] != null ||
            preferences[minChallengeRatingKey] != null ||
            (preferences[nameSearchKey] != null && preferences[nameSearchKey]?.isNotBlank() == true) ||
            (preferences[selectedClassNameKey] != null && preferences[selectedClassNameKey]?.isNotBlank() == true)
        }
    }
    
    fun hasActiveClassFiltersFlow(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[selectedClassNameKey] != null && preferences[selectedClassNameKey]?.isNotBlank() == true
        }
    }
}

