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
import com.example.hw_3.viewmodel.DnDSpellViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellDetailScreen(
    spellIndex: String,
    navController: NavHostController,
    viewModel: DnDSpellViewModel
) {
    val selectedSpell by viewModel.selectedSpell.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(spellIndex) {
        viewModel.fetchSpellDetails(spellIndex)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "✨ Детали заклинания",
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
                            onClick = { viewModel.fetchSpellDetails(spellIndex) },
                            colors = ButtonDefaults.buttonColors(containerColor = DnDGold)
                        ) {
                            Text("Повторить", color = DnDDarkBrown)
                        }
                    }
                }
            } else if (selectedSpell != null) {
                SpellDetailContent(
                    spell = selectedSpell!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun SpellDetailContent(spell: com.example.hw_3.data.DnDSpell, modifier: Modifier = Modifier) {
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
                    text = spell.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = DnDGold,
                    textAlign = TextAlign.Center
                )
                spell.level?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Уровень $it",
                        style = MaterialTheme.typography.titleMedium,
                        color = DnDBeige
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Описание
        spell.desc?.let { desc ->
            InfoCard(title = "Описание") {
                desc.forEach { paragraph ->
                    Text(
                        text = paragraph,
                        color = DnDBeige,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        spell.castingTime?.let {
            InfoCard(title = "Время накладывания") {
                Text(it, color = DnDBeige)
            }
        }

        spell.range?.let {
            InfoCard(title = "Дистанция") {
                Text(it, color = DnDBeige)
            }
        }

        spell.duration?.let {
            InfoCard(title = "Длительность") {
                Text(it, color = DnDBeige)
            }
        }

        spell.components?.let { components ->
            InfoCard(title = "Компоненты") {
                Text(components.joinToString(", "), color = DnDBeige)
            }
        }

        spell.material?.let {
            InfoCard(title = "Материальные компоненты") {
                Text(it, color = DnDBeige)
            }
        }

        spell.concentration?.let {
            if (it) {
                InfoCard(title = "Концентрация") {
                    Text("Требуется концентрация", color = DnDBeige)
                }
            }
        }

        spell.ritual?.let {
            if (it) {
                InfoCard(title = "Ритуал") {
                    Text("Можно наложить как ритуал", color = DnDBeige)
                }
            }
        }

        spell.school?.let {
            InfoCard(title = "Школа магии") {
                Text(it.name, color = DnDBeige)
            }
        }

        spell.classes?.let { classes ->
            if (classes.isNotEmpty()) {
                InfoCard(title = "Классы") {
                    Text(
                        classes.joinToString(", ") { it.name },
                        color = DnDBeige
                    )
                }
            }
        }

        spell.higherLevel?.let { higherLevel ->
            if (higherLevel.isNotEmpty()) {
                InfoCard(title = "На более высоком уровне") {
                    higherLevel.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            color = DnDBeige,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

