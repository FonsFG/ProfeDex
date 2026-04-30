package com.example.profedex.data.model

data class Usuario(
    val username: String,
    val email: String,
    val carrera: String,
    val fotoUrl: String? = null
)
