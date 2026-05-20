package com.example.profedex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.ProfesorFB
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfesorViewModelFB: ViewModel() {
    private val db = Firebase.firestore

    private val _dataProfeDex = MutableStateFlow<List<ProfesorFB>>(emptyList())

    val dataProfeDex: StateFlow<List<ProfesorFB>> = _dataProfeDex

    var state by mutableStateOf(ProfesorFB())
    private set

    fun fetchProfesorFB(){
        db.collection("ProfeDexFB")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null ){
                    return@addSnapshotListener
                }

                val documents = mutableListOf<ProfesorFB>()
                if (querySnapshot !=  null){
                    for (document in querySnapshot){
                        val myDocument = document.toObject(ProfesorFB::class.java)
                            .copy(idDoc = document.id)
                        documents.add(myDocument)
                    }
                }

                _dataProfeDex.value = documents
            }
    }

}