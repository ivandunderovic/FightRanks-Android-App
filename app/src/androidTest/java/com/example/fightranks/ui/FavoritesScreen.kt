package com.example.fightranks.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fightranks.data.FightViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FightViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Favorites",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        if (viewModel.favorites.isEmpty()) {
            Text(
                text = "No favorites yet.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn {
                items(viewModel.favorites) { fighter ->
                    Text(
                        text = "${fighter.rank}. ${fighter.name} (${fighter.category})",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
