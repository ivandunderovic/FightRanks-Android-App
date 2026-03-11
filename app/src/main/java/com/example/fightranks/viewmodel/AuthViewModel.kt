package com.example.fightranks.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)
    val errorMessage = mutableStateOf<String?>(null)
    val isLoading = mutableStateOf(false)

    fun signIn(email: String, password: String) {
        errorMessage.value = null
        isLoading.value = true

        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                isLoading.value = false
                if (task.isSuccessful) {
                    currentUser.value = auth.currentUser
                } else {
                    errorMessage.value = task.exception?.localizedMessage ?: "Sign in failed."
                }
            }
    }

    fun signOut() {
        auth.signOut()
        currentUser.value = null
    }
}
