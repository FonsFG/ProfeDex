package profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.Evaluacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EvaluarUiState(
    val profesorNombre: String = "Ing. Roberto Díaz",
    val materia: String = "Control Automático",
    val dominio: Int = 0,
    val claridad: Int = 0,
    val dificultad: Int = 0,
    val tagsDisponibles: List<String> = listOf(
        "Puntual", "Comprometido", "Califica rápido",
        "Clase dinámica", "Evaluación justa", "Asistencia obligatoria"
    ),
    val tagsSeleccionados: Set<String> = emptySet(),
    val comentario: String = ""
)

class EvaluarProfesorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EvaluarUiState())
    val uiState: StateFlow<EvaluarUiState> = _uiState.asStateFlow()

    fun onDominioChanged(rating: Int) {
        _uiState.update { it.copy(dominio = rating) }
    }

    fun onClaridadChanged(rating: Int) {
        _uiState.update { it.copy(claridad = rating) }
    }

    fun onDificultadChanged(rating: Int) {
        _uiState.update { it.copy(dificultad = rating) }
    }

    fun onTagToggled(tag: String) {
        _uiState.update { state ->
            val nuevosTags = if (state.tagsSeleccionados.contains(tag)) {
                state.tagsSeleccionados - tag // Remover si ya existe
            } else {
                state.tagsSeleccionados + tag // Agregar si no existe
            }
            state.copy(tagsSeleccionados = nuevosTags)
        }
    }

    fun onComentarioChanged(comentario: String) {
        _uiState.update { it.copy(comentario = comentario) }
    }

    fun enviarEvaluacion() {
        val estadoActual = _uiState.value

        val nuevaEvaluacion = Evaluacion(
            profesorId = "ID_TEMPORAL",
            dominio = estadoActual.dominio,
            claridad = estadoActual.claridad,
            dificultad = estadoActual.dificultad,
            tags = estadoActual.tagsSeleccionados.toList(),
            comentario = estadoActual.comentario
        )

        // TODO: Lógica para enviar 'nuevaEvaluacion' al repositorio/API
        println("Evaluación lista para enviar: $nuevaEvaluacion")
    }
}