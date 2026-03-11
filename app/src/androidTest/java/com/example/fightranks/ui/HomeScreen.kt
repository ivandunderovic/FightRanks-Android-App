package com.example.fightranks.ui



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fightranks.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {

    val categories = listOf(
        "FLYWEIGHT",
        "BANTAMWEIGHT",
        "FEATHERWEIGHT",
        "LIGHTWEIGHT",
        "WELTERWEIGHT",
        "MIDDLEWEIGHT",
        "LIGHT HEAVYWEIGHT",
        "HEAVYWEIGHT"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("FightRanks", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        categories.forEach { category ->
            Button(
                onClick = {
                    navController.navigate(
                        Screen.Fighters.createRoute(category)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(category)
            }
        }
    }
}
