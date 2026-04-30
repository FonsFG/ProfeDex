package com.example.profedex.data.model

// Modelo de datos puro que representa la evaluación final que se enviará a la base de datos
data class Evaluacion(
    val profesorId: String,
    val dominio: Int,
    val claridad: Int,
    val dificultad: Int,
    val tags: List<String>,
    val comentario: String
)