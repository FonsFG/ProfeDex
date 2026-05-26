package com.example.profedex.data.model

sealed class BuscarUiState {
    object Cargando : BuscarUiState()
    data class Exito(
        val profesores: List<ProfesorFB>,
        val filtros: List<String>,
        val filtroActivo: String?,
        val textoBusqueda: String
    ) : BuscarUiState()
    data class Error(val mensaje: String) : BuscarUiState()
}