package com.example.hw_3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hw_3.Routes
import com.example.hw_3.cache.BadgeCache
import com.example.hw_3.data.ApiReference
import com.example.hw_3.data.database.AppDatabase
import com.example.hw_3.data.database.FavoriteEntity
import com.example.hw_3.data.preferences.FilterPreferencesManager
import com.example.hw_3.di.DnDModule
import com.example.hw_3.ui.theme.*
import com.example.hw_3.viewmodel.DnDClassViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    navController: NavHostController,
    viewModel: DnDClassViewModel,
    badgeCache: BadgeCache = DnDModule.badgeCache
) {
    val context = LocalContext.current
    val filterPreferencesManager = remember { DnDModule.getFilterPreferencesManager(context) }
    val database = remember { DnDModule.getAppDatabase(context) }
    val favoriteDao = remember { database.favoriteDao() }
    val scope = rememberCoroutineScope()
    
    val classes by viewModel.classes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val filterSettings by filterPreferencesManager.filterSettings.collectAsState(initial = com.example.hw_3.data.preferences.FilterSettings())
    val hasActiveFilters by badgeCache.hasActiveFilters.collectAsState()

    LaunchedEffect(Unit) {
        if (classes.isEmpty()) {
            viewModel.fetchClasses()
        }
    }
    
    // Загружаем фильтры при старте
    LaunchedEffect(Unit) {
        filterPreferencesManager.hasActiveClassFiltersFlow().collect { hasFilters ->
            badgeCache.setHasActiveFilters(hasFilters)
        }
    }

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
                .padding(16.dp)
        ) {
            // Заголовок с кнопкой фильтра
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚔️ Классы персонажей",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = DnDGold,
                    modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
                )
                
                // Кнопка фильтров с бейджем
                Box {
                    IconButton(
                        onClick = { navController.navigate("class_filter") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Фильтры",
                            tint = DnDGold
                        )
                    }
                    if (hasActiveFilters) {
                        Badge(
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Text("•")
                        }
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = DnDGold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Загрузка классов...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = DnDBeige
                        )
                    }
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchClasses() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DnDGold
                            )
                        ) {
                            Text("Попробовать снова", color = DnDDarkBrown)
                        }
                    }
                }
            } else if (classes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Нет классов",
                            style = MaterialTheme.typography.bodyLarge,
                            color = DnDBeige
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchClasses() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DnDGold
                            )
                        ) {
                            Text("Загрузить классы", color = DnDDarkBrown)
                        }
                    }
                }
            } else {
                // Фильтруем классы - показываем только выбранный класс
                val filteredClasses = remember(classes, filterSettings) {
                    val selectedName = filterSettings.selectedClassName
                    if (selectedName != null && selectedName.isNotBlank()) {
                        // Показываем только один выбранный класс
                        classes.firstOrNull { it.name == selectedName }?.let { listOf(it) } ?: emptyList()
                    } else {
                        classes
                    }
                }
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredClasses) { classRef ->
                        var isFavorite by remember { mutableStateOf(false) }
                        
                        LaunchedEffect(classRef.index) {
                            isFavorite = favoriteDao.isFavorite(classRef.index)
                        }
                        
                        ClassItem(
                            classRef = classRef,
                            isFavorite = isFavorite,
                            onClassClick = {
                                navController.navigate(Routes.ClassDetail.createRoute(classRef.index))
                            },
                            onFavoriteClick = {
                                scope.launch {
                                    if (isFavorite) {
                                        favoriteDao.deleteFavoriteByIndex(classRef.index)
                                        isFavorite = false
                                    } else {
                                        val gson = Gson()
                                        val basicData = mapOf(
                                            "index" to classRef.index,
                                            "name" to classRef.name,
                                            "url" to classRef.url
                                        )
                                        val jsonData = gson.toJson(basicData)
                                        val favorite = FavoriteEntity(
                                            index = classRef.index,
                                            name = classRef.name,
                                            type = "class",
                                            jsonData = jsonData
                                        )
                                        favoriteDao.insertFavorite(favorite)
                                        isFavorite = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassItem(
    classRef: ApiReference,
    isFavorite: Boolean,
    onClassClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClassClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DnDDarkGray.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(DnDGold, DnDBronze)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚔️",
                    fontSize = 32.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Название
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = classRef.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DnDGold
                )
                Text(
                    text = "Нажмите для деталей",
                    style = MaterialTheme.typography.bodySmall,
                    color = DnDBeige.copy(alpha = 0.7f)
                )
            }
            
            // Сердечко избранного
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                    tint = if (isFavorite) DnDGold else DnDBeige.copy(alpha = 0.7f)
                )
            }
            
            // Стрелка
            Text(
                text = "→",
                fontSize = 24.sp,
                color = DnDGold
            )
        }
    }
}
