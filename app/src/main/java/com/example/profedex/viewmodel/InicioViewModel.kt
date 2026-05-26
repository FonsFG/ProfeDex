package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.ProfesorFB
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InicioViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _profesoresRecomendados = MutableStateFlow<List<ProfesorFB>>(emptyList())
    val profesoresRecomendados: StateFlow<List<ProfesorFB>> = _profesoresRecomendados

    private val _profesoresPesados = MutableStateFlow<List<ProfesorFB>>(emptyList())
    val profesoresPesados: StateFlow<List<ProfesorFB>> = _profesoresPesados

    init {
        // En lugar de cargar ejemplos, escuchamos a Firebase
        fetchProfesoresDesdeFirebase()
    }

    private fun fetchProfesoresDesdeFirebase() {
        db.collection("ProfeDexFB")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val todosLosProfesores = snapshot?.mapNotNull { doc ->
                    doc.toObject(ProfesorFB::class.java).copy(idDoc = doc.id)
                } ?: emptyList()

                // Lógica para separar:
                // Los recomendados (ej: rating > 4)
                _profesoresRecomendados.value = todosLosProfesores
                    .filter { it.averageRating >= 4.0 }
                    .sortedByDescending { it.averageRating }

                // Los pesados (ej: dificultad > 3)
                _profesoresPesados.value = todosLosProfesores
                    .filter { it.difficulty >= 3.5 }
                    .sortedByDescending { it.difficulty }
            }
    }
}
