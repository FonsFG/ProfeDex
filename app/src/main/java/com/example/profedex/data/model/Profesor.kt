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
    val tags: List<String>
)

data class Estadisticas(
    val conocimiento: Int,
    val claridad: Int,
    val puntualidad: Int,
    val dificultad: Int,
    val flexibilidad: Int,
    val humor: Int
)
