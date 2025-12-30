package com.example.hw_3

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems (
    val route: String,
    val title: String? = null,
    val icon: ImageVector?=null
) {
    object Screen1 : BottomNavigationItems(
        route = "screen1",
        title = "Классы",
        icon = Icons.Filled.Person
    )
    object Screen2 : BottomNavigationItems(
        route = "screen2",
        title = "Заклинания",
        icon = Icons.Filled.AutoAwesome
    )
    object Screen3 : BottomNavigationItems(
        route = "screen3",
        title = "Монстры",
        icon = Icons.Filled.Pets
    )
}