package com.example.profedex.data.model

import com.google.firebase.firestore.PropertyName

data class ProfesorFB(
    val idDoc: String = "",
    val id: String = "", // Es mejor mantenerlo como String por consistencia
    val name: String = "",
    val photo: String = "",
    val department: String = "",
    val email: String = "",
    val descripcion: String = "",
    val averageRating: Double = 0.0,
    val difficulty: Double = 0.0,
    val tags: List<String> = emptyList(),
    val materia: String = "",
    val avatarUrl: String = ""
)
