package com.example.fightranks.navigation



sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Fighters : Screen("fighters/{category}") {
        fun createRoute(category: String) = "fighters/$category"
    }
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}
