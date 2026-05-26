package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profedex.data.model.ProfesorFB
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Resultado de moderación de un comentario.
 */
data class ResultadoModeracion(
    val aprobado: Boolean,
    val razon: String = ""
)

/**
 * ViewModel para las funciones de IA con Gemini vía Firebase AI Logic.
 * - Resumen de reseñas
 * - Chat de recomendación de profesores
 * - Moderación de comentarios (groserías, apodos, ataques)
 */
class IAViewModel : ViewModel() {

    private val modelo = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

    // ─── ESTADO: RESUMEN DE RESEÑAS ────────────────────────────────
    private val _resumen = MutableStateFlow<String?>(null)
    val resumen: StateFlow<String?> = _resumen.asStateFlow()

    private val _resumenCargando = MutableStateFlow(false)
    val resumenCargando: StateFlow<Boolean> = _resumenCargando.asStateFlow()

    // ─── ESTADO: CHAT ──────────────────────────────────────────────
    private val _respuestaChat = MutableStateFlow<String?>(null)
    val respuestaChat: StateFlow<String?> = _respuestaChat.asStateFlow()

    private val _chatCargando = MutableStateFlow(false)
    val chatCargando: StateFlow<Boolean> = _chatCargando.asStateFlow()

    // ─── 1. RESUMEN DE RESEÑAS ─────────────────────────────────────
    fun resumirReseñas(comentarios: List<String>) {
        if (comentarios.isEmpty()) {
            _resumen.value = "Aún no hay reseñas para resumir."
            return
        }
        viewModelScope.launch {
            _resumenCargando.value = true
            try {
                val prompt = """
                    Eres un asistente que resume reseñas de profesores universitarios.
                    Lee los siguientes comentarios de alumnos y genera un resumen breve
                    en español con dos secciones:

                    ✅ Pros: 2-3 puntos positivos más mencionados.
                    ⚠️ Contras: 2-3 puntos negativos más mencionados.

                    Sé objetivo, conciso y NO inventes información que no esté en los comentarios.
                    Máximo 120 palabras en total.

                    Comentarios:
                    ${comentarios.mapIndexed { i, c -> "${i + 1}. $c" }.joinToString("\n")}
                """.trimIndent()

                val respuesta = modelo.generateContent(prompt)
                _resumen.value = respuesta.text?.trim() ?: "No se pudo generar el resumen."
            } catch (e: Exception) {
                _resumen.value = "Error al generar resumen: ${e.message}"
            } finally {
                _resumenCargando.value = false
            }
        }
    }

    fun limpiarResumen() {
        _resumen.value = null
    }

    // ─── 2. CHAT DE RECOMENDACIÓN ──────────────────────────────────
    fun chatRecomendacion(pregunta: String, profesores: List<ProfesorFB>) {
        if (pregunta.isBlank()) return
        viewModelScope.launch {
            _chatCargando.value = true
            try {
                val catalogo = profesores.joinToString("\n") { p ->
                    "- ${p.name} | Depto: ${p.department} | Materias: ${p.materia.joinToString(", ")} " +
                            "| Rating: ${"%.1f".format(p.averageRating)} | Dificultad: ${"%.1f".format(p.difficulty)} " +
                            "| Tags: ${p.tags.joinToString(", ")}"
                }

                val prompt = """
                    Eres el asistente de ProfeDex, una app de reseñas de profesores de la
                    Facultad de Ingeniería de la UNAM. Responde en español, de forma breve
                    y amigable (máximo 100 palabras).

                    Usa SOLO la información del catálogo de profesores. Si la pregunta no se
                    puede responder con ese catálogo, dilo claramente.

                    Catálogo de profesores:
                    $catalogo

                    Pregunta del alumno: $pregunta
                """.trimIndent()

                val respuesta = modelo.generateContent(prompt)
                _respuestaChat.value = respuesta.text?.trim() ?: "Sin respuesta."
            } catch (e: Exception) {
                _respuestaChat.value = "Error: ${e.message}"
            } finally {
                _chatCargando.value = false
            }
        }
    }

    fun limpiarChat() {
        _respuestaChat.value = null
    }

    // ─── 3. MODERACIÓN DE COMENTARIOS ──────────────────────────────
    suspend fun moderarComentario(texto: String, nombreProfesor: String): ResultadoModeracion {
        return try {
            val prompt = """
                Eres un moderador de comentarios para una app de reseñas de profesores.
                Analiza el siguiente comentario sobre el profesor "$nombreProfesor".

                BLOQUEA el comentario si contiene:
                - Groserías o palabrotas (incluso disfrazadas tipo "p3ndej0", "wey", "pndj").
                - Apodos ofensivos hacia el profesor (referencias a su físico, etnia, edad, etc.).
                - Ataques personales, insultos directos, amenazas.
                - Contenido sexual, racista, homofóbico o discriminatorio.

                APRUEBA el comentario si es:
                - Crítica académica dura pero legítima ("explica mal", "es injusto calificando", "deja demasiada tarea").
                - Opinión negativa con argumentos respetuosos.
                - Comentario positivo.

                Comentario a analizar:
                "$texto"

                Responde ÚNICAMENTE con un JSON válido en este formato exacto, sin markdown ni texto extra:
                {"aprobado": true/false, "razon": "explicación breve si se bloquea, vacío si se aprueba"}
            """.trimIndent()

            val respuesta = modelo.generateContent(prompt)
            val raw = respuesta.text?.trim().orEmpty()

            // Quitar fences de markdown si Gemini los añade
            val limpio = raw
                .removePrefix("```json").removePrefix("```")
                .removeSuffix("```")
                .trim()

            val json = JSONObject(limpio)
            ResultadoModeracion(
                aprobado = json.optBoolean("aprobado", false),
                razon = json.optString("razon", "")
            )
        } catch (e: Exception) {
            // Si la IA falla, por seguridad NO bloqueamos al usuario:
            // se permite publicar pero podrías cambiarlo a `false` si prefieres lo opuesto.
            ResultadoModeracion(aprobado = true, razon = "Moderación no disponible: ${e.message}")
        }
    }
}
