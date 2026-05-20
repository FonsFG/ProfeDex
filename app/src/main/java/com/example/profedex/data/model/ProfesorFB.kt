package com.example.profedex.data.model

data class ProfesorFB(
    val idDoc: String = "",
    val id: String = "",
    val name: String = "",
    val photo: String = "",
    val department: String = "",
    val email: String = "",
    val descripcion: String = "",
    val averageRating: Double = 0.0,
    val difficulty: Double = 0.0,
    val tags: List<String> = emptyList(),  // lista vacía como default
    val materia: String = "",
    val avatarUrl: String = "",
)