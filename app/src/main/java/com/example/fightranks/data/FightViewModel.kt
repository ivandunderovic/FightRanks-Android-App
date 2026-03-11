package com.example.fightranks.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FightViewModel : ViewModel() {

    private val db = Firebase.firestore

    val fighters = mutableStateListOf<Fighter>()
    val favorites = mutableStateListOf<Fighter>()

    fun loadCategory(category: String) {
        fighters.clear()

        db.collection("fighters")
            .whereEqualTo("category", category)
            .orderBy("rank", Query.Direction.ASCENDING)//ulazno sortira
            .get()
            .addOnSuccessListener { result ->
                fighters.clear()
                for (doc in result) {
                    val fighter = doc.toObject(Fighter::class.java)
                    fighter.id = doc.id
                    fighters.add(fighter)
                }
                applyFavoritesToFighters()
            }
    }

    fun loadFavorites() {
        favorites.clear()

        db.collection("fighters")
            .whereEqualTo("isFavorite", true)
            .orderBy("rank", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                favorites.clear()
                for (doc in result) {
                    val fighter = doc.toObject(Fighter::class.java)
                    fighter.id = doc.id
                    favorites.add(fighter)
                }
            }
    }
    fun toggleFavorite(fighter: Fighter) {
        if (fighter.id.isBlank()) return

        val newValue = !fighter.isFavorite

        // azuriranje liste fighters odma
        val idx = fighters.indexOfFirst { it.id == fighter.id }
        if (idx != -1) {
            fighters[idx] = fighters[idx].copy(isFavorite = newValue)
        }
        //azuriranje liste favorites odma
        val j = favorites.indexOfFirst { it.id == fighter.id }
        if (newValue) {
            // dodavanje borca u favorites
            if (j == -1) favorites.add(fighter.copy(isFavorite = true))
            else favorites[j] = favorites[j].copy(isFavorite = true)
        } else {
            if (j != -1) favorites.removeAt(j)
        }

        // sprema promjenu u firestore
        db.collection("fighters")
            .document(fighter.id)
            .update("isFavorite", newValue)

    }
    private fun applyFavoritesToFighters() {
        val favIds = favorites.map { it.id }.toSet()
        for (i in fighters.indices) {
            val f = fighters[i]
            fighters[i] = f.copy(isFavorite = favIds.contains(f.id))
        }
    }


}
