package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.Profesor
import com.example.profedex.data.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfesorViewModel : ViewModel() {

    // 1. Definimos el estado privado (el que nosotros podemos modificar aquí adentro)
    private val _profesorState = MutableStateFlow<Profesor?>(null)

    // 2. Definimos el estado público (el que la UI puede leer, pero no modificar)
    val profesorState: StateFlow<Profesor?> = _profesorState.asStateFlow()

    // 3. Al iniciar el ViewModel, cargamos los datos de ejemplo
    init {
        cargarDatosDePrueba()
    }

    private fun cargarDatosDePrueba() {
        _profesorState.value = Profesor(
            id = "1",
            name = "Ing. Pepe Policia",
            photo = "https://ibb.co/Xrc1Pngx",
            department = "DIMEI",
            email = "pepe.policia@ingenieria.unam.edu",
            descripcion = "Experto en control y automatización. Sus clases son duras pero te dejan filoso para el mundo real.",
            averageRating = 9.5,
            difficulty = 8.0,
            tags = listOf("Mecatrónica", "Duro", "God", "Amigable"),
            materia = "Control II"
        )
    }

    // Aquí podrías agregar más adelante una lista de reviews
    fun getReviewsDePrueba(): List<Review> {
        return listOf(
            Review(alumno = "Fonsi", fecha = "16-03-2026", estrellas = 5, comentario = "Es el mejor profe de control, se aprende muchísimo."),
            Review(alumno = "Papu", fecha = "28-10-2025", estrellas = 1, comentario = "Sus exámenes están rudos pero es justo.")
        )
    }
}