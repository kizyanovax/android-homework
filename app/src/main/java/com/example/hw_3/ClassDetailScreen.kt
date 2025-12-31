package com.example.hw_3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hw_3.ui.InfoCard
import com.example.hw_3.core.ui.theme.*
import com.example.hw_3.viewmodel.DnDClassViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(
    classIndex: String,
    navController: NavHostController,
    viewModel: DnDClassViewModel
) {
    val selectedClass by viewModel.selectedClass.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(classIndex) {
        viewModel.fetchClassDetails(classIndex)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "⚔️ Детали класса",
                        color = DnDGold
                    )
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
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = DnDGold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Загрузка...", color = DnDBeige)
                    }
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.fetchClassDetails(classIndex) },
                            colors = ButtonDefaults.buttonColors(containerColor = DnDGold)
                        ) {
                            Text("Повторить", color = DnDDarkBrown)
                        }
                    }
                }
            } else if (selectedClass != null) {
                ClassDetailContent(
                    dndClass = selectedClass!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun ClassDetailContent(dndClass: com.example.hw_3.data.DnDClass, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Заголовок
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DnDDarkGray),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dndClass.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = DnDGold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Информация
        InfoCard(title = "Индекс") {
            Text(dndClass.index, color = DnDBeige)
        }

        dndClass.hitDie?.let {
            InfoCard(title = "Кубик жизни") {
                Text("d$it", color = DnDBeige)
            }
        }

        dndClass.savingThrows?.let { throws ->
            if (throws.isNotEmpty()) {
                InfoCard(title = "Спасброски") {
                    Text(
                        throws.joinToString(", ") { it.name },
                        color = DnDBeige
                    )
                }
            }
        }

        dndClass.subclasses?.let { subclasses ->
            if (subclasses.isNotEmpty()) {
                InfoCard(title = "Подклассы") {
                    subclasses.forEach { subclass ->
                        Text("• ${subclass.name}", color = DnDBeige)
                    }
                }
            }
        }
    }
}


