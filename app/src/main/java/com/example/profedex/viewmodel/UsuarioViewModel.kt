package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel : ViewModel() {

    private val _usuario = MutableStateFlow(Usuario())
    val usuario: StateFlow<Usuario> = _usuario

    fun actualizarNombre(nuevoNombre: String) {
        _usuario.value = _usuario.value.copy(nombre = nuevoNombre)
    }

    fun actualizarCarrera(nuevaCarrera: String) {
        _usuario.value = _usuario.value.copy(carrera = nuevaCarrera)
    }

    fun actualizarSemestre(nuevoSemestre: String) {
        _usuario.value = _usuario.value.copy(semestre = nuevoSemestre)
    }

    fun actualizarCorreo(nuevoCorreo: String) {
        _usuario.value = _usuario.value.copy(correo = nuevoCorreo)
    }

    fun actualizarAvatar(nuevoAvatar: Int) {
        _usuario.value = _usuario.value.copy(avatar = nuevoAvatar)
    }
}