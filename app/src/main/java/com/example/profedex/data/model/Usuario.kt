package com.example.profedex.data.model

data class Usuario(
    val idDoc: String = "",
    val nombre: String = "",
    val carrera: String = "",
    val semestre: String = "",
    val correo: String = "",
    val avatar: Int = 0,
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val fotoUrl: String? = null
)
