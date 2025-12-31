package com.example.hw_3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hw_3.cache.BadgeCache
import com.example.hw_3.data.preferences.FilterPreferencesManager
import com.example.hw_3.data.preferences.FilterSettings
import com.example.hw_3.di.DnDModule
import com.example.hw_3.core.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavHostController,
    badgeCache: BadgeCache = DnDModule.badgeCache
) {
    val context = LocalContext.current
    val filterPreferencesManager = remember { DnDModule.getFilterPreferencesManager(context) }
    val scope = rememberCoroutineScope()
    
    val currentSettings by filterPreferencesManager.filterSettings.collectAsState(initial = FilterSettings())
    val hasActiveFilters by badgeCache.hasActiveFilters.collectAsState()
    
    var monsterType by remember { mutableStateOf(currentSettings.monsterType ?: "") }
    var minChallengeRating by remember { mutableStateOf(currentSettings.minChallengeRating?.toString() ?: "") }
    var nameSearch by remember { mutableStateOf(currentSettings.nameSearch ?: "") }
    
    // Обновляем поля при изменении настроек
    LaunchedEffect(currentSettings) {
        monsterType = currentSettings.monsterType ?: ""
        minChallengeRating = currentSettings.minChallengeRating?.toString() ?: ""
        nameSearch = currentSettings.nameSearch ?: ""
    }
    
    // Обновляем бейдж при изменении настроек
    LaunchedEffect(currentSettings) {
        val nameSearch = currentSettings.nameSearch
        val hasFilters = currentSettings.monsterType != null ||
                currentSettings.minChallengeRating != null ||
                (nameSearch != null && nameSearch.isNotBlank())
        badgeCache.setHasActiveFilters(hasFilters)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "⚙️ Настройки фильтров",
                            color = DnDGold
                        )
                        if (hasActiveFilters) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Badge {
                                Text("•")
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = DnDGold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DnDDarkGray
                )
            )
        },
        containerColor = DnDDarkBrown
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(DnDDarkBrown, DnDDarkGray)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Тип монстра
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DnDDarkGray.copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Тип монстра",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DnDGold
                        )
                        OutlinedTextField(
                            value = monsterType,
                            onValueChange = { monsterType = it },
                            label = { Text("Например: dragon, undead, beast", color = DnDBeige) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DnDBeige,
                                unfocusedTextColor = DnDBeige,
                                focusedBorderColor = DnDGold,
                                unfocusedBorderColor = DnDBeige.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                // Минимальный рейтинг сложности
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DnDDarkGray.copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Минимальный рейтинг сложности",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DnDGold
                        )
                        OutlinedTextField(
                            value = minChallengeRating,
                            onValueChange = { 
                                if (it.isEmpty() || it.toDoubleOrNull() != null) {
                                    minChallengeRating = it
                                }
                            },
                            label = { Text("Например: 1.0, 5.5, 10.0", color = DnDBeige) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DnDBeige,
                                unfocusedTextColor = DnDBeige,
                                focusedBorderColor = DnDGold,
                                unfocusedBorderColor = DnDBeige.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                // Поиск по имени
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DnDDarkGray.copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Поиск по имени",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DnDGold
                        )
                        OutlinedTextField(
                            value = nameSearch,
                            onValueChange = { nameSearch = it },
                            label = { Text("Введите часть имени", color = DnDBeige) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DnDBeige,
                                unfocusedTextColor = DnDBeige,
                                focusedBorderColor = DnDGold,
                                unfocusedBorderColor = DnDBeige.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                filterPreferencesManager.clearFilters()
                                badgeCache.setHasActiveFilters(false)
                            }
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DnDBeige
                        )
                    ) {
                        Text("Очистить")
                    }
                    
                    Button(
                        onClick = {
                            scope.launch {
                                val settings = FilterSettings(
                                    monsterType = monsterType.takeIf { it.isNotBlank() },
                                    minChallengeRating = minChallengeRating.toDoubleOrNull(),
                                    nameSearch = nameSearch.takeIf { it.isNotBlank() }
                                )
                                filterPreferencesManager.saveFilterSettings(settings)
                                
                                // Обновляем бейдж
                                val hasFilters = settings.monsterType != null ||
                                        settings.minChallengeRating != null ||
                                        (settings.nameSearch != null && settings.nameSearch.isNotBlank())
                                badgeCache.setHasActiveFilters(hasFilters)
                            }
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DnDGold
                        )
                    ) {
                        Text("Готово", color = DnDDarkBrown, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

