package com.example.fightranks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Close

import com.example.fightranks.data.FightViewModel
import com.example.fightranks.data.Fighter

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FightViewModel
) {
    val selected = remember { mutableStateOf<Fighter?>(null) }
    var query by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Surface {
        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 18.dp, vertical = 16.dp)


        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                Text(
                    text = "Favorites",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                placeholder = { Text("Search favorites...") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            val filteredFavorites = if (query.isBlank()) viewModel.favorites
            else viewModel.favorites.filter { it.name.contains(query.trim(), ignoreCase = true) }


            if (filteredFavorites.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No favorites yet.",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap the heart on a fighter to add them here.",
                        color = Color(0xFFBDBDBD),
                        fontSize = 12.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredFavorites, key = { it.id }) { fighter ->
                        FavoriteRow(
                            fighter = fighter,
                            onToggleFavorite = {
                                viewModel.toggleFavorite(fighter)
                            },
                            onOpen = { selected.value = fighter }
                        )
                    }
                }
            }
        }

        selected.value?.let { fighter ->
            FavoriteDialog(
                fighter = fighter,
                onClose = { selected.value = null }
            )
        }
    }
}

@Composable
private fun FavoriteRow(
    fighter: Fighter,
    onToggleFavorite: () -> Unit,
    onOpen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF141414))
            .clickable(onClick = onOpen)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                if (fighter.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = fighter.imageUrl,
                        contentDescription = fighter.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("#${fighter.rank}", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column {
                Text(
                    text = "${fighter.rank}. ${fighter.name}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Record: ${fighter.record}",
                    color = Color(0xFFBDBDBD),
                    fontSize = 12.sp
                )
            }
        }

        IconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove from favorites",
                tint = Color(0xFFBDBDBD)
            )
        }
    }
}

@Composable
private fun FavoriteDialog(
    fighter: Fighter,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(onClick = onClose) { Text("Close") }
        },
        title = { Text(text = fighter.name, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (fighter.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = fighter.imageUrl,
                        contentDescription = fighter.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                Text("Category: ${fighter.category}")
                Text("Rank: ${fighter.rank}")
                Text("Record: ${fighter.record}")
                Text("Weight: ${fighter.weightKg} kg")
                Text("Height: ${fighter.heightCm} cm")
            }
        }
    )
}
