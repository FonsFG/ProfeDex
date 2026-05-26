package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.BuscarUiState
import com.example.profedex.data.model.ProfesorFB
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BuscarProfesoresViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _uiState = MutableStateFlow<BuscarUiState>(BuscarUiState.Cargando)
    val uiState: StateFlow<BuscarUiState> = _uiState.asStateFlow()

    private var todosProfesores = emptyList<ProfesorFB>()

    private val filtrosDisponibles = listOf(
        "Mucha tarea", "Exámenes difíciles", "Barco",
        "Explica bien", "Asistencia obligatoria", "Proyectos"
    )

    init {
        fetchProfesoresRealtime()
    }

    private fun fetchProfesoresRealtime() {
        db.collection("ProfeDexFB")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = BuscarUiState.Error(error.message ?: "Error desconocido")
                    return@addSnapshotListener
                }

                todosProfesores = snapshot?.mapNotNull { doc ->
                    doc.toObject(ProfesorFB::class.java).copy(idDoc = doc.id)
                } ?: emptyList()

                actualizarUi()
            }
    }

    fun onBusquedaCambia(texto: String) {
        actualizarUi(texto = texto)
    }

    fun onFiltroSeleccionado(filtro: String) {
        val estadoActual = _uiState.value
        if (estadoActual is BuscarUiState.Exito) {
            val nuevoFiltro = if (estadoActual.filtroActivo == filtro) null else filtro
            actualizarUi(texto = estadoActual.textoBusqueda, filtro = nuevoFiltro)
        }
    }

    private fun actualizarUi(texto: String = "", filtro: String? = null) {
        val filtrados = todosProfesores.filter { profesor ->
            val coincideTexto = texto.isBlank() ||
                    profesor.name.contains(texto, ignoreCase = true) ||
                    profesor.materia.contains(texto, ignoreCase = true)

            val coincideFiltro = filtro == null || profesor.tags.contains(filtro)
            coincideTexto && coincideFiltro
        }

        _uiState.value = BuscarUiState.Exito(
            profesores = filtrados,
            filtros = filtrosDisponibles,
            filtroActivo = filtro,
            textoBusqueda = texto
        )
    }
}
