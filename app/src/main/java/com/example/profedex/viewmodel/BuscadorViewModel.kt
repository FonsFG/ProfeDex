package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.BuscarUiState
import com.example.profedex.data.model.Profesor

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BuscarProfesoresViewModel : ViewModel() {
        // StateFlow = un contenedor que notifica a la UI cuando cambia
        // (como una suscripción de revista — te llega cuando hay novedad)
        private val _uiState = MutableStateFlow<BuscarUiState>(BuscarUiState.Cargando)
        val uiState: StateFlow<BuscarUiState> = _uiState.asStateFlow()

        // Datos dummy (falsos) — se reemplazarán con API real después
        private val todosProfesores = listOf(
            Profesor(
                id = "1",
                name = "Juan Pérez",
                photo = "",
                department = "Matemáticas",
                email = "juan@example.com",
                descripcion = "Experto en cálculo.",
                averageRating = 4.0,
                difficulty = 3.0,
                tags = listOf("Mucha tarea", "Explica bien"),
                materia = "Cálculo II"
            ),
            Profesor(
                id = "2",
                name = "Cristian Servín",
                photo = "",
                department = "Física",
                email = "cristian@example.com",
                descripcion = "Especialista en mecánica.",
                averageRating = 4.5,
                difficulty = 4.0,
                tags = listOf("Exámenes difíciles"),
                materia = "Física I"
            ),
            Profesor(
                id = "3",
                name = "María López",
                photo = "",
                department = "Física",
                email = "maria@example.com",
                descripcion = "Enfoque en termodinámica.",
                averageRating = 5.0,
                difficulty = 2.0,
                tags = listOf("Explica bien", "Barco"),
                materia = "Física I"
            ),
            Profesor(
                id = "4",
                name = "Roberto Sánchez",
                photo = "",
                department = "Matemáticas",
                email = "roberto@example.com",
                descripcion = "Álgebra avanzada.",
                averageRating = 3.5,
                difficulty = 4.5,
                tags = listOf("Proyectos", "Asistencia obligatoria"),
                materia = "Álgebra Lineal"
            ),
            Profesor(
                id = "5",
                name = "Ana García",
                photo = "",
                department = "Computación",
                email = "ana@example.com",
                descripcion = "Algoritmos y estructuras.",
                averageRating = 4.8,
                difficulty = 2.5,
                tags = listOf("Barco", "Explica bien"),
                materia = "Programación"
            )
        )

        private val filtrosDisponibles = listOf(
            "Mucha tarea", "Exámenes difíciles", "Barco",
            "Explica bien", "Asistencia obligatoria", "Proyectos"
        )

        // Se ejecuta automáticamente al crear el ViewModel
        init {
            cargarProfesores()
        }

        fun cargarProfesores() {
            // Aquí irá la llamada real al Repository → API/BD en el futuro
            _uiState.value = BuscarUiState.Exito(
                profesores = todosProfesores,
                filtros = filtrosDisponibles,
                filtroActivo = null,
                textoBusqueda = ""
            )
        }

        // Evento: el usuario escribe en la barra de búsqueda
        fun onBusquedaCambia(texto: String) {
            _uiState.update { estadoActual ->
                if (estadoActual is BuscarUiState.Exito) {
                    val profesoresFiltrados = filtrarProfesores(
                        texto = texto,
                        filtro = estadoActual.filtroActivo
                    )
                    estadoActual.copy(
                        textoBusqueda = texto,
                        profesores = profesoresFiltrados
                    )
                } else estadoActual
            }
        }

        // Evento: el usuario toca un chip de filtro
        fun onFiltroSeleccionado(filtro: String) {
            _uiState.update { estadoActual ->
                if (estadoActual is BuscarUiState.Exito) {
                    // Si toca el mismo filtro activo, lo desactiva (toggle)
                    val nuevoFiltro = if (estadoActual.filtroActivo == filtro) null else filtro
                    estadoActual.copy(
                        filtroActivo = nuevoFiltro,
                        profesores = filtrarProfesores(estadoActual.textoBusqueda, nuevoFiltro)
                    )
                } else estadoActual
            }
        }

        // Lógica de filtrado (solo texto por ahora, sin lógica compleja)
        private fun filtrarProfesores(texto: String, filtro: String?): List<Profesor> {
            return todosProfesores.filter { profesor ->
                val coincideTexto = texto.isBlank() ||
                        profesor.name.contains(texto, ignoreCase = true) ||
                        profesor.materia.contains(texto, ignoreCase = true)

                val coincideFiltro = filtro == null ||
                        profesor.tags.any { it == filtro }

                coincideTexto && coincideFiltro
            }
        }
    }