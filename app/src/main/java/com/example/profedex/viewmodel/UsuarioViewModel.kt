package com.example.profedex.viewmodel

import androidx.lifecycle.ViewModel
import com.example.profedex.data.model.Usuario
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthResult {
    object Idle : AuthResult()
    object Loading : AuthResult()
    data class Success(val username: String) : AuthResult()
    data class Error(val mensaje: String) : AuthResult()
}

class UsuarioViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val coleccion = "ProfeDexUsuario"

    private val _usuario = MutableStateFlow(Usuario())
    val usuario: StateFlow<Usuario> = _usuario.asStateFlow()

    private val _loginResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val loginResult: StateFlow<AuthResult> = _loginResult.asStateFlow()

    private val _registroResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val registroResult: StateFlow<AuthResult> = _registroResult.asStateFlow()

    /**
     * Verifica usuario y contraseña contra Firestore.
     */
    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginResult.value = AuthResult.Error("Ingresa tu usuario y contraseña")
            return
        }
        _loginResult.value = AuthResult.Loading
        db.collection(coleccion)
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { snapshot ->
                val doc = snapshot.documents.firstOrNull()
                if (doc == null) {
                    _loginResult.value = AuthResult.Error("El usuario no existe")
                    return@addOnSuccessListener
                }
                val user = doc.toObject(Usuario::class.java)?.copy(idDoc = doc.id)
                if (user == null) {
                    _loginResult.value = AuthResult.Error("No se pudo leer el usuario")
                    return@addOnSuccessListener
                }
                if (user.password != password) {
                    _loginResult.value = AuthResult.Error("Contraseña incorrecta")
                    return@addOnSuccessListener
                }
                _usuario.value = user
                _loginResult.value = AuthResult.Success(user.username)
            }
            .addOnFailureListener { e ->
                _loginResult.value = AuthResult.Error("Error de conexión: ${e.localizedMessage ?: "desconocido"}")
            }
    }

    /**
     * Registra un nuevo usuario validando que el username no exista.
     */
    fun registrar(nuevoUsuario: Usuario) {
        if (nuevoUsuario.username.isBlank() || nuevoUsuario.password.isBlank()) {
            _registroResult.value = AuthResult.Error("Usuario y contraseña son obligatorios")
            return
        }
        if (nuevoUsuario.password.length < 4) {
            _registroResult.value = AuthResult.Error("La contraseña debe tener al menos 4 caracteres")
            return
        }
        _registroResult.value = AuthResult.Loading
        db.collection(coleccion)
            .whereEqualTo("username", nuevoUsuario.username)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    _registroResult.value = AuthResult.Error("Ese usuario ya está en uso")
                    return@addOnSuccessListener
                }
                db.collection(coleccion).add(nuevoUsuario)
                    .addOnSuccessListener { docRef ->
                        val creado = nuevoUsuario.copy(idDoc = docRef.id)
                        _usuario.value = creado
                        _registroResult.value = AuthResult.Success(creado.username)
                    }
                    .addOnFailureListener { e ->
                        _registroResult.value = AuthResult.Error("No se pudo crear la cuenta: ${e.localizedMessage ?: "desconocido"}")
                    }
            }
            .addOnFailureListener { e ->
                _registroResult.value = AuthResult.Error("Error de conexión: ${e.localizedMessage ?: "desconocido"}")
            }
    }

    fun resetLoginResult() {
        _loginResult.value = AuthResult.Idle
    }

    fun resetRegistroResult() {
        _registroResult.value = AuthResult.Idle
    }

    /**
     * Carga los datos del usuario desde Firestore buscando por username.
     */
    fun fetchUsuario(username: String) {
        db.collection(coleccion)
            .whereEqualTo("username", username)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val userDoc = snapshot?.documents?.firstOrNull()
                if (userDoc != null) {
                    _usuario.value = userDoc.toObject(Usuario::class.java)?.copy(idDoc = userDoc.id) ?: Usuario(username = username)
                } else {
                    // Si el usuario no existe en la BD, mantenemos el username local
                    _usuario.value = _usuario.value.copy(username = username)
                }
            }
    }

    /**
     * Guarda o actualiza los datos del usuario en Firestore.
     */
    fun guardarUsuario() {
        val user = _usuario.value
        if (user.idDoc.isNotEmpty()) {
            db.collection(coleccion).document(user.idDoc).set(user)
        } else {
            // Si es un usuario nuevo, lo agregamos
            db.collection(coleccion).add(user).addOnSuccessListener { docRef ->
                _usuario.value = user.copy(idDoc = docRef.id)
            }
        }
    }

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
