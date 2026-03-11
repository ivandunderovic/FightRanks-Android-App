package com.example.fightranks.ui



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fightranks.data.FightViewModel

@Composable
fun FightersScreen(
    navController: NavController,
    viewModel: FightViewModel,
    category: String
) {
    LaunchedEffect(Unit) {
        viewModel.loadCategory(category)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = category,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(viewModel.fighters) { fighter ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text("${fighter.rank}. ${fighter.name}")

                    IconButton(onClick = {
                        viewModel.toggleFavorite(fighter)
                    }) {
                        Icon(
                            imageVector =
                                if (fighter.isFavorite)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
