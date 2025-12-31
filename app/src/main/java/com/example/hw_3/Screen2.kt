package com.example.hw_3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hw_3.data.database.AppDatabase
import com.example.hw_3.data.database.FavoriteEntity
import com.example.hw_3.di.DnDModule
import com.example.hw_3.core.ui.theme.*
import com.example.hw_3.viewmodel.DnDSpellViewModel

@Composable
fun Screen2(
    navController: NavHostController,
    viewModel: DnDSpellViewModel
) {
    val context = LocalContext.current
    val database = remember { DnDModule.getAppDatabase(context) }
    val favoriteDao = remember { database.favoriteDao() }
    
    val favorites by remember {
        favoriteDao.getFavoritesByType("class")
    }.collectAsState(initial = emptyList())

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
            // Заголовок
            Text(
                text = "★ Избранные классы",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = DnDGold,
                modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
            )

            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = DnDBeige.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "Нет избранных классов",
                            style = MaterialTheme.typography.titleLarge,
                            color = DnDBeige
                        )
                        Text(
                            text = "Добавьте классы в избранное на вкладке 'Классы'",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DnDBeige.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favorites) { favorite ->
                        FavoriteClassItem(
                            favorite = favorite,
                            onItemClick = {
                                navController.navigate("class_detail/${favorite.index}")
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
fun FavoriteClassItem(
    favorite: FavoriteEntity,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
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
            // Иконка избранного
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = DnDGold
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Название
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = favorite.name,
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
            
            // Стрелка
            Text(
                text = "→",
                fontSize = 24.sp,
                color = DnDGold
            )
        }
    }
}
