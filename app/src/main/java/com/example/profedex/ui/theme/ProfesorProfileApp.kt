package com.example.profedex.ui.theme

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profedex.ui.screens.EvaluarProfesorScreen
import com.example.profedex.ui.screens.InicioScreen
import com.example.profedex.ui.screens.ProfesorProfileScreen
import com.example.profedex.viewmodel.ProfesorViewModel

@Composable
fun ProfesorProfileApp() {
    // El estado de la navegación vive aquí ahora
    var pantallaActual by remember { mutableStateOf("inicio") }

    // Obtenemos los ViewModels necesarios
    val profeViewModel: ProfesorViewModel = viewModel()
    val profesor by profeViewModel.profesorState.collectAsState()

    // El "Switch" de pantallas
    when (pantallaActual) {
        "inicio" -> {
            InicioScreen(
                onBuscarClick = { /* Lógica de buscar */ },
                onProfesorClick = { id ->
                    pantallaActual = "profe"
                }
            )
        }
        "profe" -> {
            profesor?.let { profe ->
                ProfesorProfileScreen(
                    professor = profe,
                    reviews = profeViewModel.getReviewsDePrueba(), // <--- AQUÍ ESTÁ LA PIEZA QUE FALTABA
                    onBackClick = {
                        pantallaActual = "inicio"
                    },
                    onEvaluarClick = { pantallaActual = "evaluar" }
                )
            }
        }
        "evaluar" -> {
            EvaluarProfesorScreen(
                onBackClick = {
                    // Al dar atrás en evaluar, regresamos al perfil
                    pantallaActual = "perfil"
                }
            )
        }
    }
}