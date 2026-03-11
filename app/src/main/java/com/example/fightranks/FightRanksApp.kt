package com.example.fightranks.ui



import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fightranks.navigation.Screen
import com.example.fightranks.ui.FavoritesScreen
import com.example.fightranks.ui.FightersScreen
import com.example.fightranks.ui.HomeScreen
import com.example.fightranks.ui.ProfileScreen
import com.example.fightranks.viewmodel.AuthViewModel
import com.example.fightranks.data.FightViewModel

@Composable
fun FightRanksApp(
    navController: NavHostController = rememberNavController(),
    fightViewModel: FightViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != null && !currentRoute.startsWith("fighters/")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home().route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home().route) {
                HomeScreen(
                    navController = navController
                )
            }
            composable(Screen.Favorites().route) {
                FavoritesScreen(
                    navController = navController,
                    viewModel = fightViewModel
                )
            }
            composable(Screen.Profile().route) {
                ProfileScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable(Screen.Fighters().route) { backStack ->
                val category = backStack.arguments?.getString("category") ?: ""
                FightersScreen(
                    navController = navController,
                    viewModel = fightViewModel,
                    category = category
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    val items = listOf(
        Screen.Home() to Icons.Filled.Home,
        Screen.Favorites() to Icons.Filled.Star,
        Screen.Profile() to Icons.Filled.Person
    )

    NavigationBar {
        items.forEach { (screen, icon) ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = screen.route) },
                label = null
            )
        }
    }
}
