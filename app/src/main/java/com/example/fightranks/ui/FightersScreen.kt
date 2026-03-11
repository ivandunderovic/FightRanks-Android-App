package com.example.fightranks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.fightranks.data.FightViewModel
import com.example.fightranks.data.Fighter

@Composable
fun FightersScreen(
    navController: NavController,
    viewModel: FightViewModel,
    category: String
) {
    val selected = remember { mutableStateOf<Fighter?>(null) }

    LaunchedEffect(category) {
        viewModel.loadFavorites() //povuci favorite iz baze
        viewModel.loadCategory(category) //onda povuci borce u kategorije
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
                    text = category,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (viewModel.fighters.isEmpty()) {
                Text(
                    text = "No fighters found.",
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = viewModel.fighters,
                        key = { it.id }
                    ) { fighter ->
                        FighterRow(
                            fighter = fighter,
                            onToggleFavorite = { viewModel.toggleFavorite(fighter) },
                            onOpen = { selected.value = fighter }
                        )
                    }
                }
            }

        }
        }
        selected.value?.let { fighter ->
            FighterDialog(
                fighter = fighter,
                onClose = { selected.value = null }
            )
        }
    }

@Composable
private fun FighterRow(
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
                imageVector = if (fighter.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (fighter.isFavorite) Color(0xFFFF4D6D) else Color.White
            )
        }
    }
}

@Composable
private fun FighterDialog(
    fighter: Fighter,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            Button(onClick = onClose) { Text("Close") }
        },
        title = {
            Text(text = fighter.name, fontWeight = FontWeight.Bold)
        },
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
