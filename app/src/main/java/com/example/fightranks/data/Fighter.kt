package com.example.fightranks.data

data class Fighter(
    var id: String = "",
    val name: String = "",
    val rank: Int = 0,
    val category: String = "",
    val wins: Int = 0,
    val losses: Int = 0,
    val weightKg: Double = 0.0,
    val heightCm: Int = 0,
    val imageUrl: String = "",
    var isFavorite: Boolean = false
) {
    val record: String get() = "$wins-$losses"
}
