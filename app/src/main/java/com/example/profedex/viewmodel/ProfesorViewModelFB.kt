package com.example.profedex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.ProfesorFB
import com.example.profedex.data.model.Review
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfesorViewModelFB : ViewModel() {

    private val db = Firebase.firestore

    private var profesorListener: ListenerRegistration? = null
    private var reviewListener: ListenerRegistration? = null

    private val _dataProfeDex = MutableStateFlow<List<ProfesorFB>>(emptyList())
    val dataProfeDex: StateFlow<List<ProfesorFB>> = _dataProfeDex.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var state by mutableStateOf(ProfesorFB())
        private set

    init {
        fetchProfesorFB()
    }
    
    fun seleccionarProfesor(profesor: ProfesorFB) {
        state = profesor
    }

    fun fetchProfesorFB() {
        profesorListener?.remove()
        profesorListener = db.collection("ProfeDexFB")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) return@addSnapshotListener

                val documents = querySnapshot?.mapNotNull { document ->
                    document.toObject(ProfesorFB::class.java).copy(idDoc = document.id)
                } ?: emptyList()
                
                _dataProfeDex.value = documents
            }
    }

    fun guardarNuevoProfesor(
        nuevoProfe: ProfesorFB,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        _isLoading.value = true
        
        val listaActual = _dataProfeDex.value
        val ultimoId = listaActual.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 10
        val siguienteId = (ultimoId + 1).toString()

        val profeFinal = nuevoProfe.copy(
            id = siguienteId,
            photo = "URL"
        )

        db.collection("ProfeDexFB")
            .add(profeFinal)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onFailure(e)
            }
    }

    fun fetchReviews(idDoc: String) {
        if (idDoc.isBlank()) {
            _reviews.value = emptyList()
            return
        }
        _isLoading.value = true
        reviewListener?.remove()
        reviewListener = db.collection("ProfeDexFB")
            .document(idDoc)
            .collection("reviews")
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) return@addSnapshotListener

                val lista = snapshot?.mapNotNull { doc ->
                    doc.toObject(Review::class.java).copy(id = doc.id)
                } ?: emptyList()
                
                _reviews.value = lista
            }
    }

    override fun onCleared() {
        super.onCleared()
        profesorListener?.remove()
        reviewListener?.remove()
    }

    fun actualizarProfesor(
        profesorId: String,
        profesorActualizado: ProfesorFB,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("profesores") // Reemplaza por el nombre real de tu colección
            .document(profesorId)
            .set(profesorActualizado) // Al usar .set() actualiza todo el documento con las nuevas listas y promedios
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
