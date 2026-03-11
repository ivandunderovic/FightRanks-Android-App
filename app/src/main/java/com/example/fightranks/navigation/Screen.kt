package com.example.fightranks.navigation

sealed class Screen(val route: String) {

    class Home : Screen("home")
    class Favorites : Screen("favorites")
    class Profile : Screen("profile")

    class Fighters : Screen("fighters/{category}") {
        fun createRoute(category: String) = "fighters/$category"
    }
}
