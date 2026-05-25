package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.ProfesorFB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InicioViewModel : ViewModel() { //Aqui va lo de la Api

    private val _profesoresRecomendados = MutableStateFlow<List<ProfesorFB>>(emptyList())
    val profesoresRecomendados: StateFlow<List<ProfesorFB>> = _profesoresRecomendados

    private val _profesoresPesados = MutableStateFlow<List<ProfesorFB>>(emptyList())
    val profesoresPesados: StateFlow<List<ProfesorFB>> = _profesoresPesados

    init {
        cargarDatosEjemplo()
    }

    private fun cargarDatosEjemplo() {
        _profesoresRecomendados.value = listOf(
            ProfesorFB(
                id = "1",
                name = "Dr. Ejemplo Recomendado",
                photo = "foto_profesor_recomendado",
                department = "Ingeniería",
                email = "ejemplo@unam.mx",
                descripcion = "Excelente profesor",
                averageRating = 4.8,
                difficulty = 2.0,
                tags = listOf("Claro", "Puntual"),
                materia = "Cálculo I"
            )
        )
        _profesoresPesados.value = listOf(
            ProfesorFB(
                id = "2",
                name = "Dr. Ejemplo Pesado",
                photo = "foto_profesor_pesado",
                department = "Ingeniería",
                email = "ejemplo2@unam.mx",
                descripcion = "Muy exigente",
                averageRating = 2.5,
                difficulty = 3.5,
                tags = listOf("Difícil", "Exigente"),
                materia = "Física III"
            )
        )
    }
}
