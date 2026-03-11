package com.example.fightranks.data



import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FightViewModel : ViewModel() {

    private val db = Firebase.firestore

    var fighters = mutableStateListOf<Fighter>()
        private set

    var favorites = mutableStateListOf<Fighter>()
        private set

    fun loadCategory(category: String) {
        fighters.clear()

        db.collection("fighters")
            .whereEqualTo("category", category)
            .orderBy("rank")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val fighter = doc.toObject(Fighter::class.java)
                    fighter.id = doc.id
                    fighters.add(fighter)
                }
            }
    }

    fun loadFavorites() {
        favorites.clear()

        db.collection("fighters")
            .whereEqualTo("isFavorite", true)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val fighter = doc.toObject(Fighter::class.java)
                    fighter.id = doc.id
                    favorites.add(fighter)
                }
            }
    }

    fun toggleFavorite(fighter: Fighter) {
        fighter.isFavorite = !fighter.isFavorite

        db.collection("fighters")
            .document(fighter.id)
            .update("isFavorite", fighter.isFavorite)
    }
}
