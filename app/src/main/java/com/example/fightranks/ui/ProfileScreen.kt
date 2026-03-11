package com.example.fightranks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fightranks.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val user = authViewModel.currentUser.value

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (user == null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            } else{
                Spacer(modifier = Modifier.height(60.dp))
            }

            if (user != null) {
                LoggedInCard(
                    email = user.email ?: "Logged in",
                    onSignOut = { authViewModel.signOut() }
                )
            } else {
                LoginCard(authViewModel = authViewModel)
            }
        }
    }
}

@Composable
private fun LoginCard(authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by authViewModel.isLoading
    val error = authViewModel.errorMessage.value

    Column(
        modifier = Modifier
            .widthIn(max = 340.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF141414))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Email", color = Color.White, fontSize = 14.sp)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text("Password", color = Color.White, fontSize = 14.sp)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Button(
            onClick = { authViewModel.signIn(email, password) },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.height(18.dp))
            } else {
                Text("Sign In")
            }
        }

        if (error != null) {
            Text(text = error, color = Color(0xFFFF4D6D), fontSize = 12.sp)
        }

        Text(
            text = "Forgot password?",
            color = Color(0xFFBDBDBD),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun LoggedInCard(email: String, onSignOut: () -> Unit) {
    Column(
        modifier = Modifier
            .widthIn(max = 340.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF141414))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Signed in as: $email", color = Color(0xFFBDBDBD))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onSignOut) { Text("Sign out") }
    }
}
