package com.example.profedex.data.model

data class Usuario(
    val nombre: String = "",
    val carrera: String = "",
    val semestre: String = "",
    val correo: String = "",
    val avatar: Int = 0
    val username: String,
    val email: String,
    val carrera: String,
    val fotoUrl: String? = null
)
