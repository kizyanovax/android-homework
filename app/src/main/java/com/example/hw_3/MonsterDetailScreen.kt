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
import com.example.hw_3.ui.theme.*
import com.example.hw_3.viewmodel.DnDMonsterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonsterDetailScreen(
    monsterIndex: String,
    navController: NavHostController,
    viewModel: DnDMonsterViewModel
) {
    val selectedMonster by viewModel.selectedMonster.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(monsterIndex) {
        viewModel.fetchMonsterDetails(monsterIndex)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "👹 Детали монстра",
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
                            onClick = { viewModel.fetchMonsterDetails(monsterIndex) },
                            colors = ButtonDefaults.buttonColors(containerColor = DnDGold)
                        ) {
                            Text("Повторить", color = DnDDarkBrown)
                        }
                    }
                }
            } else if (selectedMonster != null) {
                MonsterDetailContent(
                    monster = selectedMonster!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun MonsterDetailContent(monster: com.example.hw_3.data.DnDMonster, modifier: Modifier = Modifier) {
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
                    text = monster.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = DnDGold,
                    textAlign = TextAlign.Center
                )
                monster.size?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$it ${monster.type ?: ""}",
                        style = MaterialTheme.typography.titleMedium,
                        color = DnDBeige
                    )
                }
                monster.alignment?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DnDBeige.copy(alpha = 0.8f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Характеристики
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard("Сила", monster.strength?.toString() ?: "—", modifier = Modifier.weight(1f))
            StatCard("Ловкость", monster.dexterity?.toString() ?: "—", modifier = Modifier.weight(1f))
            StatCard("Телосл.", monster.constitution?.toString() ?: "—", modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard("Интеллект", monster.intelligence?.toString() ?: "—", modifier = Modifier.weight(1f))
            StatCard("Мудрость", monster.wisdom?.toString() ?: "—", modifier = Modifier.weight(1f))
            StatCard("Харизма", monster.charisma?.toString() ?: "—", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        monster.armorClass?.firstOrNull()?.value?.let {
            InfoCard(title = "Класс брони") {
                Text("$it", color = DnDBeige)
            }
        }

        monster.hitPoints?.let {
            InfoCard(title = "Очки жизни") {
                Text("$it ${monster.hitDice ?: ""}", color = DnDBeige)
            }
        }

        monster.challengeRating?.let {
            InfoCard(title = "Рейтинг сложности") {
                Text("$it", color = DnDBeige)
            }
        }

        monster.xp?.let {
            InfoCard(title = "Опыт") {
                Text("$it XP", color = DnDBeige)
            }
        }

        monster.speed?.let { speed ->
            InfoCard(title = "Скорость") {
                speed.walk?.let { Text("Ходьба: $it", color = DnDBeige) }
                speed.fly?.let { Text("Полет: $it", color = DnDBeige) }
                speed.swim?.let { Text("Плавание: $it", color = DnDBeige) }
                speed.burrow?.let { Text("Рытье: $it", color = DnDBeige) }
            }
        }

        monster.languages?.let {
            InfoCard(title = "Языки") {
                Text(it, color = DnDBeige)
            }
        }

        monster.damageVulnerabilities?.let { vulnerabilities ->
            if (vulnerabilities.isNotEmpty()) {
                InfoCard(title = "Уязвимости") {
                    Text(vulnerabilities.joinToString(", "), color = DnDBeige)
                }
            }
        }

        monster.damageResistances?.let { resistances ->
            if (resistances.isNotEmpty()) {
                InfoCard(title = "Сопротивления") {
                    Text(resistances.joinToString(", "), color = DnDBeige)
                }
            }
        }

        monster.damageImmunities?.let { immunities ->
            if (immunities.isNotEmpty()) {
                InfoCard(title = "Иммунитеты к урону") {
                    Text(immunities.joinToString(", "), color = DnDBeige)
                }
            }
        }

        monster.actions?.let { actions ->
            if (actions.isNotEmpty()) {
                InfoCard(title = "Действия") {
                    actions.forEach { action ->
                        action.name?.let {
                            Text(
                                text = "• $it",
                                fontWeight = FontWeight.Bold,
                                color = DnDGold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        action.desc?.let {
                            Text(
                                text = it,
                                color = DnDBeige,
                                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DnDDarkGray.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = DnDBeige.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DnDGold
            )
        }
    }
}

