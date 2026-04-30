package com.example.profedex.data.model

//evaluación final que se enviará a la base de datos
data class Evaluacion(
    val profesorId: String,
    val dominio: Int,
    val claridad: Int,
    val dificultad: Int,
    val tags: List<String>,
    val comentario: String
)