package com.example.hw_3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hw_3.ui.theme.*
import com.example.hw_3.viewmodel.DnDSpellViewModel

@Composable
fun Screen2(
    navController: NavHostController,
    viewModel: DnDSpellViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DnDDarkBrown, DnDDarkGray)
                )
            )
    ) {
        // Пустой экран
    }
}
