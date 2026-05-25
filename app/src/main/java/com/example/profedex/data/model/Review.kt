package com.example.profedex.data.model

/**
 * Modelo para las reseñas de los profesores.
 * Se incluyen valores por defecto para compatibilidad con Firestore (toObject).
 */
data class Review(
    val id: String = "",
    val alumno: String = "",
    val fecha: String = "",
    val estrellas: Int = 0,
    val comentario: String = ""
)
