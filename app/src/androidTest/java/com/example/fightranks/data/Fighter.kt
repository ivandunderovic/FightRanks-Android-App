package com.example.fightranks.data



data class Fighter(
    var id: String = "",
    val name: String = "",
    val rank: Int = 0,
    val category: String = "",
    var isFavorite: Boolean = false
)
