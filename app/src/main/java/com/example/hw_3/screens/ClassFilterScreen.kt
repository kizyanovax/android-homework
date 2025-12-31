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
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.preferences.FilterPreferencesManager
import com.example.hw_3.data.preferences.FilterSettings
import com.example.hw_3.di.DnDModule
import com.example.hw_3.core.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassFilterScreen(
    navController: NavHostController,
    classes: List<ApiReference>,
    badgeCache: BadgeCache = DnDModule.badgeCache
) {
    val context = LocalContext.current
    val filterPreferencesManager = remember { DnDModule.getFilterPreferencesManager(context) }
    val scope = rememberCoroutineScope()
    
    val currentSettings by filterPreferencesManager.filterSettings.collectAsState(initial = FilterSettings())
    val hasActiveFilters by badgeCache.hasActiveFilters.collectAsState()
    
    var expanded by remember { mutableStateOf(false) }
    var selectedClassName by remember { mutableStateOf(currentSettings.selectedClassName ?: "") }
    
    // Обновляем выбранное значение при изменении настроек
    LaunchedEffect(currentSettings) {
        selectedClassName = currentSettings.selectedClassName ?: ""
    }
    
    // Обновляем бейдж при изменении настроек
    LaunchedEffect(currentSettings) {
        val selectedName = currentSettings.selectedClassName
        val hasFilters = selectedName != null && selectedName.isNotBlank()
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
                            "⚙️ Фильтр классов",
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
                // Выпадающее меню для выбора класса
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
                            text = "Выберите класс",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DnDGold
                        )
                        
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedClassName,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Класс", color = DnDBeige) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DnDBeige,
                                    unfocusedTextColor = DnDBeige,
                                    focusedBorderColor = DnDGold,
                                    unfocusedBorderColor = DnDBeige.copy(alpha = 0.5f)
                                )
                            )
                            
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .background(DnDDarkGray)
                                    .heightIn(max = 400.dp)
                            ) {
                                // Опция "Все классы"
                                DropdownMenuItem(
                                    text = { Text("Все классы", color = DnDBeige) },
                                    onClick = {
                                        selectedClassName = ""
                                        expanded = false
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = DnDBeige
                                    )
                                )
                                
                                // Показываем все классы
                                if (classes.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text("Загрузка классов...", color = DnDBeige.copy(alpha = 0.7f)) },
                                        onClick = { },
                                        enabled = false,
                                        colors = MenuDefaults.itemColors(
                                            textColor = DnDBeige.copy(alpha = 0.7f)
                                        )
                                    )
                                } else {
                                    classes.forEach { classRef ->
                                        DropdownMenuItem(
                                            text = { Text(classRef.name, color = DnDBeige) },
                                            onClick = {
                                                selectedClassName = classRef.name
                                                expanded = false
                                            },
                                            colors = MenuDefaults.itemColors(
                                                textColor = DnDBeige
                                            )
                                        )
                                    }
                                }
                            }
                        }
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
                                filterPreferencesManager.clearClassFilters()
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
                                    selectedClassName = selectedClassName.takeIf { it.isNotBlank() }
                                )
                                filterPreferencesManager.saveFilterSettings(
                                    currentSettings.copy(selectedClassName = settings.selectedClassName)
                                )
                                
                                // Обновляем бейдж
                                val hasFilters = settings.selectedClassName != null && settings.selectedClassName.isNotBlank()
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

