package com.example.profedex.data.model

data class ProfesorFB(
    val idDoc: String = "",
    val id: String = "", 
    val name: String = "",
    val photo: String = "URL",
    val department: String = "",
    val email: String = "",
    val descripcion: String = "",
    val averageRating: Double = 0.0,
    val difficulty: Double = 0.0,
    val tags: List<String> = emptyList(),
    val materia: List<String> = emptyList(),
    val avatarUrl: String = "",
    val listaRating: List<Int> = emptyList(),
    val listaDifficulty: List<Int> = emptyList(),
    val listaComment: List<String> = emptyList()
)
