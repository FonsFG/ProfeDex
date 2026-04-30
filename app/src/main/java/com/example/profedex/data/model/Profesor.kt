package com.example.profedex.data.model

data class Profesor(
    val id: String,
    val name: String,
    val photo: String,
    val department: String,
    val email: String,
    val descripcion: String,
    val averageRating: Double,
    val difficulty: Double,
    val tags: List<String>,

    val materia: String,
    val avatarUrl: String = "",   // URL de imagen (vacío = usa placeholder)
)
