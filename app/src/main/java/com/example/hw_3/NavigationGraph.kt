package com.example.hw_3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hw_3.screens.ClassFilterScreen
import com.example.hw_3.screens.FavoritesScreen
import com.example.hw_3.screens.FilterScreen
import com.example.hw_3.screens.Screen1
import com.example.hw_3.viewmodel.DnDClassViewModel
import com.example.hw_3.viewmodel.DnDMonsterViewModel
import com.example.hw_3.viewmodel.DnDSpellViewModel
import com.example.hw_3.core.Routes
import com.example.hw_3.profile.screens.ProfileScreen
import com.example.hw_3.profile.screens.EditProfileScreen
import com.example.hw_3.profile.viewmodel.ProfileViewModel


@Composable
fun NavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController, startDestination = Routes.Welcome.route) {
        composable(Routes.Welcome.route) {
            onBottomBarVisibilityChanged(false)
            Welcome(navController = navController)
        }
        composable(Routes.Screen1.route) {
            onBottomBarVisibilityChanged(true)
            val classViewModel: DnDClassViewModel = viewModel()
            Screen1(navController = navController, viewModel = classViewModel)
        }
        composable(Routes.ClassFilter.route) {
            onBottomBarVisibilityChanged(false)
            val classViewModel: DnDClassViewModel = viewModel()
            val classes by classViewModel.classes.collectAsState()
            
            // Загружаем классы, если они еще не загружены
            LaunchedEffect(Unit) {
                if (classes.isEmpty()) {
                    classViewModel.fetchClasses()
                }
            }
            
            ClassFilterScreen(
                navController = navController,
                classes = classes
            )
        }
        composable(Routes.Screen2.route) {
            onBottomBarVisibilityChanged(true)
            val spellViewModel: DnDSpellViewModel = viewModel()
            Screen2(navController = navController, viewModel = spellViewModel)
        }
        composable(Routes.Profile.route) {
            onBottomBarVisibilityChanged(true)
            val profileViewModel: ProfileViewModel = viewModel()
            ProfileScreen(navController = navController, viewModel = profileViewModel)
        }
        composable(Routes.EditProfile.route) {
            onBottomBarVisibilityChanged(false)
            val profileViewModel: ProfileViewModel = viewModel()
            EditProfileScreen(
                navController = navController,
                viewModel = profileViewModel,
                mainActivityClass = MainActivity::class.java
            )
        }
        composable(Routes.Filter.route) {
            onBottomBarVisibilityChanged(false)
            FilterScreen(navController = navController)
        }
        composable(
            route = Routes.Favorites.route,
            arguments = listOf(navArgument("type") {
                type = androidx.navigation.NavType.StringType
            })
        ) { backStackEntry ->
            onBottomBarVisibilityChanged(false)
            val type = backStackEntry.arguments?.getString("type") ?: "monster"
            FavoritesScreen(navController = navController, type = type)
        }
        composable(
            route = Routes.ClassDetail.route,
            arguments = listOf(navArgument("classIndex") {
                type = androidx.navigation.NavType.StringType
            })
        ) { backStackEntry ->
            onBottomBarVisibilityChanged(false)
            val classIndex = backStackEntry.arguments?.getString("classIndex") ?: ""
            val classViewModel: DnDClassViewModel = viewModel()
            ClassDetailScreen(
                classIndex = classIndex,
                navController = navController,
                viewModel = classViewModel
            )
        }
        composable(
            route = Routes.SpellDetail.route,
            arguments = listOf(navArgument("spellIndex") {
                type = androidx.navigation.NavType.StringType
            })
        ) { backStackEntry ->
            onBottomBarVisibilityChanged(false)
            val spellIndex = backStackEntry.arguments?.getString("spellIndex") ?: ""
            val spellViewModel: DnDSpellViewModel = viewModel()
            SpellDetailScreen(
                spellIndex = spellIndex,
                navController = navController,
                viewModel = spellViewModel
            )
        }
        composable(
            route = Routes.MonsterDetail.route,
            arguments = listOf(navArgument("monsterIndex") {
                type = androidx.navigation.NavType.StringType
            })
        ) { backStackEntry ->
            onBottomBarVisibilityChanged(false)
            val monsterIndex = backStackEntry.arguments?.getString("monsterIndex") ?: ""
            val monsterViewModel: DnDMonsterViewModel = viewModel()
            MonsterDetailScreen(
                monsterIndex = monsterIndex,
                navController = navController,
                viewModel = monsterViewModel
            )
        }
    }
}